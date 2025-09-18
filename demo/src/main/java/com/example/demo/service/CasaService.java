package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Casa;
import com.example.demo.repository.CasaRepository;

@Service
public class CasaService 
{
    
     private final CasaRepository casaRepository;
  
     //Construtor para uma injeção de dependência
     //injeção de dependência é uma tranferência de uma tarefa
     //de criação do objeto para outra entidade(objeto/banco) eu usar diretamente a dependência
     //Depências = bibliotecas de código que um projeto precisa para funcionar corretamente
  
    public CasaService(CasaRepository casaRepository)
    {
        this.casaRepository = casaRepository;
    }
  
    //Mudança feita para o List ficar com paginação

     public Page<Casa> listarCasasComFiltro( Double precoMax,Integer quartosMin,Integer banheirosMin, 
     Double precoMin, Integer quartosMax, Integer banheirosMax,Pageable pageable) 

     {  
    
    // Usamos o findAll(peageable) (que já faz o LIMIT e OFFSET no SQL)
     
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
        .toList();

    // Calcula os índices de início e fim da página
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), filtradas.size());

    // Garante que não dê erro se a página pedida for maior que o tamanho da lista
    List<Casa> paginadas = (start <= end) ? filtradas.subList(start, end) : List.of();

    // Retorna o Page respeitando paginação e total de elementos
    return new PageImpl<>(paginadas, pageable, filtradas.size());
 }


    //Metodo para cadastrar uma nova casa 
    public Casa cadastrarCasa (Casa casa)
    {
     
        //Verifica se o endereço já existe, validar campos, etc.
        return casaRepository.save(casa);

    }
  
  //Mudança feita para o page
  public Page<Casa> listarTodasAsCasas(Pageable pageable) 
  {
    return casaRepository.findAll(pageable);
  }


    //Método para buscar uma casa por ID
    public Optional<Casa> buscarCasaPorId(Long id)
    {
        return casaRepository.findById(id);
    }
 
    //Método para atualizar uma casa existente
    public Casa atualizarCasa(Long id, Casa casaAtualizada)
    {
        return casaRepository.findById(id).map(casaExistente ->{

            casaExistente.setNome(casaAtualizada.getNome());
            casaExistente.setEndereco(casaAtualizada.getEndereco());
            casaExistente.setDescricao(casaAtualizada.getDescricao());
            casaExistente.setLatitude(casaAtualizada.getLatitude());
            casaExistente.setLongitude(casaAtualizada.getLongitude());
            casaExistente.setQuantidadeQuartos(casaAtualizada.getQuantidadeQuartos());
            casaExistente.setQuantidadeBanheiros(casaAtualizada.getQuantidadeBanheiros());

            casaExistente.setPrecoDiaria(casaAtualizada.getPrecoDiaria());
            casaExistente.setCapacidadePessoas(casaAtualizada.getCapacidadePessoas());

            return casaRepository.save(casaExistente);
        
        }).orElseThrow(()-> new RuntimeException("Casa não encontrada com ID: " + id)); 
    }
        
          //Metódo para deletar a casa
          public void deletarCasa(Long id)
          {
          
          // Uma verificação para garantir que a casa existe antes de deletar
          if (!casaRepository.existsById(id))
          {
            throw new RuntimeException("Casa não encontrada com ID: " + id);
          }

          casaRepository.deleteById(id);
          }

        
}   