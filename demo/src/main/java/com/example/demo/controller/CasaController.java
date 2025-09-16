package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     
     //Endpoint para cadastrar uma nova casa
     @PostMapping
     
     public ResponseEntity<Casa> cadastrarCasa(@RequestBody Casa casa) 
     
     {
        Casa novaCasa = casaService.cadastrarCasa(casa);
        return new ResponseEntity<>(novaCasa,HttpStatus.CREATED);
     }
     
     //Endpoint para buscar uma casa por Id (Get/api/casas)
     @GetMapping
     
     public ResponseEntity<List<Casa>> listarTodasAsCasas() 
     {
         List<Casa> casas = casaService.listarTodasAsCasas();
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
