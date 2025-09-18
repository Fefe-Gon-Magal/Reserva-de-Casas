package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.Casa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO 
{
    private Casa casa;
    private String nomeCliente;
    private String emailCliente;  
    private String cpfCliente;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer quantidadePessoas;
}
