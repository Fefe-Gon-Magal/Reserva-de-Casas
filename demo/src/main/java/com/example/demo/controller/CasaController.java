package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CasaRequestDTO;
import com.example.demo.dto.CasaResponseDTO;
import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.model.Casa;
import com.example.demo.service.CasaService;
import com.example.demo.service.ViaCepService;

/**
 * Controlador REST para gerenciar operações relacionadas a casas.
 * Expõe endpoints para cadastrar, listar, buscar, atualizar e deletar casas.
 */
@RestController
@RequestMapping("/api/casas")
public class CasaController 
{

    private final CasaService casaService;
    private final ViaCepService cepService;

    /**
     * Construtor para injeção de dependências dos serviços CasaService e ViaCepService.
     * @param casaService Serviço responsável pela lógica de negócio das casas.
     * @param cepService Serviço responsável pela consulta de CEPs via ViaCEP.
     */
    public CasaController(CasaService casaService, ViaCepService cepService) 
    {
        this.casaService = casaService;
        this.cepService = cepService;
    }

    /**
     * Endpoint para cadastrar uma nova casa.
     * Recebe um {@link CasaRequestDTO} no corpo da requisição e retorna um {@link CasaResponseDTO}.
     * O endereço pode ser preenchido automaticamente via ViaCEP se um CEP for fornecido.
     * @param casaRequestDTO DTO contendo os dados da casa a ser cadastrada.
     * @return ResponseEntity com o {@link CasaResponseDTO} da casa criada e status HTTP 201 (Created).
     */
   
   
    @PostMapping
    public ResponseEntity<CasaResponseDTO> cadastrarCasa(@RequestBody CasaRequestDTO casaRequestDTO) 
    {
      // O serviço CasaService agora é responsável por toda a lógica de criação e preenchimento do endereço/CEP.
      CasaResponseDTO resposta = casaService.cadastrarCasa(casaRequestDTO);
      return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as casas com suporte a paginação.
     * @param pageable Objeto Pageable para definir a paginação (página, tamanho, ordenação).
     * @return ResponseEntity com uma página de entidades {@link Casa} e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<Page<Casa>> listarTodasAsCasas(Pageable pageable) 
    {
        Page<Casa> casas = casaService.listarTodasAsCasas(pageable);
        return new ResponseEntity<>(casas, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar informações de endereço utilizando um CEP via ViaCEP.
     * @param cep O CEP a ser consultado.
     * @return ResponseEntity com os dados do endereço {@link ViaCepResponseDTO} e status HTTP 200 (OK),
     *         ou status HTTP 404 (Not Found) se o CEP não for encontrado ou for inválido.
     */
    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepResponseDTO> buscarEnderecoPorCep(@PathVariable String cep) 
    {
        ViaCepResponseDTO endereco = cepService.buscarCep(cep);
        if (endereco == null || endereco.getCep() == null) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(endereco, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar uma casa específica por ID.
     * @param id O ID da casa a ser buscada.
     * @return ResponseEntity com a entidade {@link Casa} e status HTTP 200 (OK),
     *         ou status HTTP 404 (Not Found) se a casa não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Casa> buscarCasaPorId(@PathVariable Long id) 
    {
        return casaService.buscarCasaPorId(id)
                .map(casa -> new ResponseEntity<>(casa, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint para atualizar uma casa existente.
     * @param id O ID da casa a ser atualizada.
     * @param casaAtualizada A entidade {@link Casa} com os dados atualizados.
     * @return ResponseEntity com a entidade {@link Casa} atualizada e status HTTP 200 (OK),
     *         ou status HTTP 404 (Not Found) se a casa não for encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Casa> atualizarCasa(@PathVariable Long id, @RequestBody Casa casaAtualizada) 
    {
        try 
        {
            Casa casa = casaService.atualizarCasa(id, casaAtualizada);
            return new ResponseEntity<>(casa, HttpStatus.OK);
        } 
        catch (RuntimeException e) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para listar casas com filtros e suporte a paginação.
     * Permite filtrar por preço máximo/mínimo, quantidade de quartos e banheiros.
     * @param precoMax 
     * @param quartosMin 
     * @param banheirosMin 
     * @param precoMin 
     * @param quartosMax
     * @param banheirosMax
     * @param pageable Objeto Pageable para definir a paginação.
     * @return ResponseEntity com uma página de entidades {@link Casa} filtradas e status HTTP 200 (OK).
     */
    @GetMapping("/filtrar")
    public ResponseEntity<Page<Casa>> listarCasasFiltradas(
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Integer quartosMin,
            @RequestParam(required = false) Integer banheirosMin,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Integer quartosMax,
            @RequestParam(required = false) Integer banheirosMax,
            Pageable pageable) {

        Page<Casa> casas = casaService.listarCasasComFiltro(
                precoMax, quartosMin, banheirosMin, precoMin,
                quartosMax, banheirosMax, pageable);

        return new ResponseEntity<>(casas, HttpStatus.OK);
    }

    /**
     * Endpoint para deletar uma casa por ID.
     * @param id O ID da casa a ser deletada.
     * @return ResponseEntity com status HTTP 204 (No Content) se a casa for deletada com sucesso,
     *         ou status HTTP 404 (Not Found) se a casa não for encontrada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCasa(@PathVariable Long id) 
    {
        try 
        {
            casaService.deletarCasa(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } 
        catch (RuntimeException e) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}