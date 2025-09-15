package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Table (name = "casas")
@NoArgsConstructor
@Data
@AllArgsConstructor

public class Casa 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false )
    private String nome;

    @Column(nullable = false) 
    private String endereco;

    @Column(length = 1000) // Define um tamanho máximo para descrição

    private String descricao;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Integer quantidadeQuartos;

    @Column(nullable = false)
    private Integer quantidadeBanheiros;

    @Column(nullable = false)
    private Double precoDiaria;
    
    @Column(nullable = false)
    private Integer capacidadePessoas;



}
