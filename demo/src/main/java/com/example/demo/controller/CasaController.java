package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CasaRequestDTO;
import com.example.demo.dto.CasaResponseDTO;
import com.example.demo.model.Casa;
import com.example.demo.service.CasaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping ("/api/casas")
public class CasaController 
{
     private final CasaService casaService;
     
     public CasaController(CasaService casaService)
     {
        this.casaService = casaService;
     }
     
     //Endpoint para cadastrar uma nova casa, com o DTO
     @PostMapping
     
     public ResponseEntity<CasaResponseDTO> cadastrarCasa(@RequestBody CasaRequestDTO casaRequestDTO) 
     
     
        // Converte CasaRequestDTO para a entidade Casa
        // O ID é nulo porque será gerado pelo serviço/banco de dados
     {
         Casa casa = new Casa ();
         
         casa.setNome (casaRequestDTO.getNome());
         casa.setEndereco(casaRequestDTO.getEndereco());
         casa.setCapacidadePessoas(casaRequestDTO.getCapacidadePessoas());
         casa.setQuantidadeBanheiros(casaRequestDTO.getQuantidadeBanheiros());
         casa.setQuantidadeQuartos(casaRequestDTO.getQuantidadeQuartos());
         casa.setDescricao(casaRequestDTO.getDescricao());
         casa.setLatitude(casaRequestDTO.getLatitude());
         casa.setLongitude(casaRequestDTO.getLongitude());
         casa.setPrecoDiaria(casaRequestDTO.getPrecoDiaria());
      
        Casa novaCasa = casaService.cadastrarCasa(casa);
        
        //Converte o DTO antes de devolver 
        CasaResponseDTO resposta = new CasaResponseDTO();


        resposta.setNome(novaCasa.getNome());
        resposta.setEndereco(novaCasa.getEndereco());
        resposta.setCapacidadePessoas(novaCasa.getCapacidadePessoas());
        resposta.setPrecoDiaria(novaCasa.getPrecoDiaria());
        resposta.setQuantidadeBanheiros(novaCasa.getQuantidadeBanheiros());
        resposta.setQuantidadeQuartos(novaCasa.getQuantidadeQuartos());
        resposta.setDescricao(casaRequestDTO.getDescricao());
        resposta.setLatitude(casaRequestDTO.getLatitude());
        resposta.setLongitude(casaRequestDTO.getLongitude());
        
        
        return new ResponseEntity<>(resposta,HttpStatus.CREATED);
     }
     
     //Mudança feita para o Page
     @GetMapping
     public ResponseEntity<Page<Casa>> listarTodasAsCasas(Pageable pageable)
     {
    
      Page<Casa> casas = casaService.listarTodasAsCasas(pageable);
      return new ResponseEntity<>(casas, HttpStatus.OK);
    
    }
     // Endpoint para buscar uma casa por ID (Get/ api/ casas/{id})
     @GetMapping("/{id}")
     
     public ResponseEntity<Casa>buscarCasaPorId(@PathVariable Long id) 
     
     {
        return casaService.buscarCasaPorId(id)
        .map(casa -> new ResponseEntity<>(casa, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
     }
     
     //Endpoint para atualizar uma casa (PUT/ api/casas/{id})
     @PutMapping("/{id}")
    
     public ResponseEntity<Casa> atualizarCasa(@PathVariable Long id, @RequestBody Casa casaAtualizada)

     {

        try
        {
          Casa casa = casaService.atualizarCasa(id,casaAtualizada);
          return new ResponseEntity<>(casa, HttpStatus.OK);
        }
        
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
     
    } 

      // Endpoint para listar casas com filtros, também com paginação (GET /api/casas/filtrar)
      @GetMapping("/filtrar")
      public ResponseEntity<Page<Casa>> listarCasasFiltradas
      (
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Integer quartosMin,
            @RequestParam(required = false) Integer banheirosMin,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Integer quartosMax,
            @RequestParam(required = false) Integer banheirosMax,
            //Mudança feita para a paginação
            Pageable pageable
           
      ) 
    
      {
      
        Page<Casa> casas = casaService.listarCasasComFiltro
        (precoMax, quartosMin, banheirosMin, precoMin,
        quartosMax,banheirosMax,pageable);
        
        return new ResponseEntity<>(casas, HttpStatus.OK);
       }



       
       //Endpoint para deletar uma casa (DELETE/api/casas/{id})
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
