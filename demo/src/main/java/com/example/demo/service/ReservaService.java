package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Casa;
import com.example.demo.model.Reserva;
import com.example.demo.repository.CasaRepository;
import com.example.demo.repository.ReservaRepository;

@Service
public class ReservaService 
{
    
    private final ReservaRepository reservaRepository;
    private final CasaRepository casaRepository;

    public ReservaService(ReservaRepository reservaRepository,
    CasaRepository casaRepository)
    {
        this.reservaRepository = reservaRepository;
        this.casaRepository = casaRepository;
    }

    //Método para criar uma nova reserva

    public Reserva criarReserva(Reserva reserva)
    
    {

        //Verifica se a casa existe 
        Casa casa = casaRepository.findById(reserva.getCasa().getId())
       .orElseThrow(() -> new RuntimeException("Casa não encontrada com ID: " + reserva.getCasa().getId()));  
                               
        //Garante que a casa associada é a do banco de dados
        reserva.setCasa(casa);                                                                                                                          
    
        if (reserva.getCheckIn().isAfter(reserva.getCheckOut()))
        {
        throw new IllegalArgumentException("Data de chek-in não pode ser depois da data de check-out. ");
        }
        if (reserva.getCheckIn().isBefore(LocalDate.now()))
        {
         throw new IllegalArgumentException("Data de check-in deve ser no futuro. ");

        }


         return reservaRepository.save(reserva);
 
    }
        
         public List<Reserva> listarTodasAsReservas() 
         {
         return reservaRepository.findAll();

         }

        public Optional<Reserva> buscarReservaPorId(Long id) 
        {
        return reservaRepository.findById(id);
        }

        public void cancelarReserva(Long id) 
        {
        
            if (!reservaRepository.existsById(id)) 
            {
            throw new RuntimeException("Reserva não encontrada com ID: " + id);
            }
            reservaRepository.deleteById(id);
    }
}







