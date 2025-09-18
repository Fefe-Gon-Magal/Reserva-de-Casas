package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasaRequestDTO 
{
    private String nome;
    private String endereco;
    private Double precoDiaria;
    private Integer quantidadeQuartos;
    private Integer quantidadeBanheiros;
    private String descricao;
    private Double latitude;
    private Double longitude;
    private Integer capacidadePessoas;
}
