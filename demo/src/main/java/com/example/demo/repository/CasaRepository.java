package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Casa;

@Repository
public interface CasaRepository  extends JpaRepository<Casa, Long>
{
    


}
