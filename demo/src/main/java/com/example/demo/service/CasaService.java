package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CasaRequestDTO;
import com.example.demo.dto.CasaResponseDTO;
import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.model.Casa;
import com.example.demo.repository.CasaRepository;

/**
 * Serviço responsável pela lógica de negócio da entidade {@link Casa}.
 * Gerencia operações de CRUD e filtragem, além de integrar com o serviço ViaCEP.
 */
@Service
public class CasaService 
{

    private final CasaRepository casaRepository;
    private final ViaCepService viaCepService;

    /**
     * Construtor para injeção de dependências dos repositórios e serviços necessários.
     * @param casaRepository Repositório para acesso aos dados das casas.
     * @param viaCepService Serviço para consulta de CEPs.
     */
    public CasaService(CasaRepository casaRepository, ViaCepService viaCepService) {
        this.casaRepository = casaRepository;
        this.viaCepService = viaCepService;
    }

    /**
     * Cadastra uma nova casa no sistema.
     * Realiza a conversão de {@link CasaRequestDTO} para {@link Casa} e preenche o endereço
     * automaticamente via ViaCEP se um CEP for fornecido.
     * @param dto O DTO contendo os dados da casa a ser cadastrada.
     * @return Um {@link CasaResponseDTO} representando a casa cadastrada.
     */
    public CasaResponseDTO cadastrarCasa (CasaRequestDTO dto) 
    {
        Casa casa = new Casa();
        casa.setNome(dto.getNome());
        casa.setQuantidadeQuartos(dto.getQuantidadeQuartos());
        casa.setQuantidadeBanheiros(dto.getQuantidadeBanheiros());
        casa.setDescricao(dto.getDescricao());
        casa.setLatitude(dto.getLatitude());
        casa.setLongitude(dto.getLongitude());
        casa.setPrecoDiaria(dto.getPrecoDiaria());
        casa.setCapacidadePessoas(dto.getCapacidadePessoas());

        // Se um CEP for fornecido no DTO, tenta buscar o endereço via ViaCEP
        if (dto.getCep() != null && !dto.getCep().trim().isEmpty()) 
        
        {
            
          ViaCepResponseDTO viaCep = viaCepService.buscarCep(dto.getCep());
           if (viaCep != null && viaCep.getLogradouro() != null) 
          {
          String enderecoCompleto = viaCep.getLogradouro() + ", "
                                    + viaCep.getBairro() + " - "
                                    + viaCep.getLocalidade() + "/"
                                    + viaCep.getUf();
                casa.setEndereco(enderecoCompleto);
                casa.setCep(dto.getCep()); // Define o CEP na entidade Casa
          } 
          else 
          {
          // Se o CEP não for encontrado ou inválido, usa o endereço fornecido no DTO (se houver)
           casa.setEndereco(dto.getEndereco());
           casa.setCep(dto.getCep()); // Mantém o CEP original mesmo que não tenha sido encontrado
          }
        } 
         else 
        {
            // Se nenhum CEP for fornecido, usa o endereço do DTO
            casa.setEndereco(dto.getEndereco());
            casa.setCep(null); // Garante que o CEP seja nulo se não foi fornecido
        }

        Casa novaCasa = casaRepository.save(casa);
        return convertToResponseDTO(novaCasa);
    }

    /**
     * Lista todas as casas com suporte a paginação.
     * @param pageable Objeto Pageable para definir a paginação.
     * @return Uma página de entidades {@link Casa}.
     */
    public Page<Casa> listarTodasAsCasas(Pageable pageable) 
    {
        return casaRepository.findAll(pageable);
    }

    /**
     * Busca uma casa específica por ID.
     * @param id O ID da casa a ser buscada.
     * @return Um {@link Optional} contendo a casa, se encontrada.
     */
    public Optional<Casa> buscarCasaPorId(Long id) {
        return casaRepository.findById(id);
    }

    /**
     * Atualiza uma casa existente no sistema.
     * @param id O ID da casa a ser atualizada.
     * @param casaAtualizada A entidade {@link Casa} com os dados a serem atualizados.
     * @return A entidade {@link Casa} atualizada.
     * @throws RuntimeException se a casa não for encontrada.
     */
        public Casa atualizarCasa(Long id, Casa casaAtualizada) 
        {
             return casaRepository.findById(id).map(casaExistente -> 
            {
            casaExistente.setNome(casaAtualizada.getNome());
            casaExistente.setEndereco(casaAtualizada.getEndereco());
            casaExistente.setDescricao(casaAtualizada.getDescricao());
            casaExistente.setLatitude(casaAtualizada.getLatitude());
            casaExistente.setLongitude(casaAtualizada.getLongitude());
            casaExistente.setQuantidadeQuartos(casaAtualizada.getQuantidadeQuartos());
            casaExistente.setQuantidadeBanheiros(casaAtualizada.getQuantidadeBanheiros());
            casaExistente.setPrecoDiaria(casaAtualizada.getPrecoDiaria());
            casaExistente.setCapacidadePessoas(casaAtualizada.getCapacidadePessoas());
            casaExistente.setCep(casaAtualizada.getCep()); // Adicionado o CEP na atualização
            return casaRepository.save(casaExistente);
            }).orElseThrow(() -> new RuntimeException("Casa não encontrada com ID: " + id));
        }

    /**
     * Deleta uma casa do sistema pelo seu ID.
     * @param id O ID da casa a ser deletada.
     * @throws RuntimeException se a casa não for encontrada.
     */
    public void deletarCasa(Long id) 
    {
        if (!casaRepository.existsById(id)) 
        {
            throw new RuntimeException("Casa não encontrada com ID: " + id);
        }
        casaRepository.deleteById(id);
    }

    /**
     * Lista casas com filtros e suporte a paginação.
     * Os filtros são aplicados em memória após a busca inicial paginada.
      @param precoMax 
      @param quartosMin 
      @param banheirosMin 
      @param precoMin 
      @param quartosMax
      @param banheirosMax 
      @param pageable Objeto Pageable para definir a paginação.
      @return Uma página de entidades {@link Casa} filtradas.
     */
    public Page<Casa> listarCasasComFiltro( Double precoMax, Integer quartosMin, Integer banheirosMin,
    Double precoMin, Integer quartosMax, Integer banheirosMax, Pageable pageable) 
    {

        // Busca todas as casas paginadas (primeiro vem todas as páginas do banco)
        Page<Casa> casas = casaRepository.findAll(pageable);

        // Aplica os filtros "em memória"
        List<Casa> filtradas = casas.getContent().stream()
                .filter(casa -> precoMax == null || casa.getPrecoDiaria() <= precoMax)
                .filter(casa -> precoMin == null || casa.getPrecoDiaria() >= precoMin)
                .filter(casa -> quartosMax == null || casa.getQuantidadeQuartos() <= quartosMax)
                .filter(casa -> quartosMin == null || casa.getQuantidadeQuartos() >= quartosMin)
                .filter(casa -> banheirosMax == null || casa.getQuantidadeBanheiros() <= banheirosMax)
                .filter(casa -> banheirosMin == null || casa.getQuantidadeBanheiros() >= banheirosMin)
                .collect(Collectors.toList());

        // Calcula os índices de início e fim da página
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtradas.size());

        // Garante que não dê erro se a página pedida for maior que o tamanho da lista
        List<Casa> paginadas = (start <= end) ? filtradas.subList(start, end) : List.of();

        // Retorna o Page respeitando paginação e total de elementos
        return new PageImpl<>(paginadas, pageable, filtradas.size());
    }

    /**
     * Converte uma entidade {@link Casa} para um {@link CasaResponseDTO}.
     * @param casa A entidade Casa a ser convertida.
     * @return O DTO de resposta da casa.
     */
    private CasaResponseDTO convertToResponseDTO(Casa casa) 
    {
        CasaResponseDTO resposta = new CasaResponseDTO();
        resposta.setId(casa.getId()); // Adicionado o ID ao DTO de resposta
        resposta.setCep(casa.getCep());
        resposta.setNome(casa.getNome());
        resposta.setEndereco(casa.getEndereco());
        resposta.setCapacidadePessoas(casa.getCapacidadePessoas());
        resposta.setPrecoDiaria(casa.getPrecoDiaria());
        resposta.setQuantidadeBanheiros(casa.getQuantidadeBanheiros());
        resposta.setQuantidadeQuartos(casa.getQuantidadeQuartos());
        resposta.setDescricao(casa.getDescricao());
        resposta.setLatitude(casa.getLatitude());
        resposta.setLongitude(casa.getLongitude());
        return resposta;
    }
}