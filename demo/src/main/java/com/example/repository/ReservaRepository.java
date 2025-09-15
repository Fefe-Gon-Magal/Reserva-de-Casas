package com.example.repository;

import org.springframework.stereotype.Repository;

import com.example.model.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>
{
    
}
