package com.example.demo.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //Para filtrar diretamente no banco de dados
    @Query("SELECT c FROM Casa c " +
           "WHERE (:precoMax IS NULL OR c.precoDiaria <= :precoMax) " +
           "AND (:precoMin IS NULL OR c.precoDiaria >= :precoMin) " +
           "AND (:quartosMax IS NULL OR c.quantidadeQuartos <= :quartosMax) " +
           "AND (:quartosMin IS NULL OR c.quantidadeQuartos >= :quartosMin) " +
           "AND (:banheirosMax IS NULL OR c.quantidadeBanheiros <= :banheirosMax) " +
           "AND (:banheirosMin IS NULL OR c.quantidadeBanheiros >= :banheirosMin)")
    Page<Casa> filtrarCasas(
        @Param("precoMax") Double precoMax,
        @Param("precoMin") Double precoMin,
        @Param("quartosMax") Integer quartosMax,
        @Param("quartosMin") Integer quartosMin,
        @Param("banheirosMax") Integer banheirosMax,
        @Param("banheirosMin") Integer banheirosMin,
        Pageable pageable
    );


}
