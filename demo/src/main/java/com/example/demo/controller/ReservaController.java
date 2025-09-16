package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;

import java.net.http.HttpClient;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping ("/api/reservas")

public class ReservaController 

{
    private final ReservaService reservaService;


    public ReservaController (ReservaService reservaService)
    {
        this.reservaService = reservaService;
    }

    //Endpoint para criar uma nova reserva (POST /api/reservas)
    @PostMapping
    
    public ResponseEntity<Reserva> criarReserva(@RequestBody Reserva reserva)
    {
        try
        {
            Reserva novaReserva = reservaService.criarReserva(reserva);
            return new ResponseEntity<>(novaReserva, HttpStatus.CREATED);
        }
        
        catch (RuntimeException e)
        {
            //Captura exenções do serviço (ex: Casa não encontrada)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        
        }
    
    }
    //Endpoint para listar todas as reservas (GET/api/reservas)
    @GetMapping

    public ResponseEntity<List<Reserva>> listarTodasAsReservas()
    {
         List<Reserva> reservas = reservaService.listarTodasAsReservas();
          
         return new ResponseEntity<>(reservas, HttpStatus.OK);
    
    }
    //Endpoit pra buscar uma reserva por ID (GET/api/reservas/{id})
    @GetMapping("/{id}")
    
    public ResponseEntity<Reserva> buscarReservaPorId (@PathVariable Long id)  
    {

       return reservaService.buscarReservaPorId(id)
       .map(reserva -> new ResponseEntity<>(reserva,HttpStatus.OK))
       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id)
    {
        try
        {
            reservaService.cancelarReserva(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (RuntimeException e)
        {

          return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    
    }
    

}
