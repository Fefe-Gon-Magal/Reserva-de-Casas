package com.example.model;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // Para Coluna de chave estrangeira
import jakarta.persistence.ManyToOne; //Para relacionamento com Casa
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marca esta classe como entidade JPA.
@Table // Dafine o nome da tabela no banco de dados
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor
@AllArgsConstructor

public class Reserva 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    
    @ManyToOne // Define um relacionamento de muitos para um com a entidade casa 

    @JoinColumn(name = "casa_id", nullable = false) //Coluna de chava estrangeira para Casa

    private Casa casa;

    @Column (nullable = false) //campo obrigatório
    private String nomeCliente;

    @Column (nullable = false)
    private String emailCliente;

    @Column (nullable = false)
    private String cpfCliente;

    @Column (nullable = false)
    private LocalDate checkIn;

    @Column (nullable = false)
    private LocalDate checkOut;

    @Column (nullable = false)
    private Integer quantidadePessoas;




}
