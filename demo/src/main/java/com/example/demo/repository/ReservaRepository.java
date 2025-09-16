package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Casa;
import com.example.demo.model.Reserva;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>
{    
     //Metódo que verifica sobreposição de reservas
     List<Reserva> findByCasaAndCheckInBeforeAndCheckOutAfter(
        Casa casa,
        LocalDate checkOut,
        LocalDate checkIn
    );
}
