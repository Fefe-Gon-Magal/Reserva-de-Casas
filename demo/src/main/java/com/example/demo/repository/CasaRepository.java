package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Casa;

@Repository
public interface CasaRepository  extends JpaRepository<Casa, Long>
{
     // Novo método para buscar casas por preço diária menor ou igual a um valor
    List<Casa> findByPrecoDiariaLessThanEqual(Double precoDiaria);

    // Novo método para buscar casas por quantidade de quartos
    List<Casa> findByQuantidadeQuartos(Integer quantidadeQuartos);

    // Novo método para buscar casas por quantidade de banheiros
    List<Casa> findByQuantidadeBanheiros(Integer quantidadeBanheiros);


}
