package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.feing.ViaCepFeignClient;

// Criação de um Service para uma API dia 26.09.25
@Service
public class ViaCepService 
{
  
    private final ViaCepFeignClient viaCepFeignClient; 

    public ViaCepService(ViaCepFeignClient viaCepFeignClient)
    {
        this.viaCepFeignClient = viaCepFeignClient;
    }

    public ViaCepResponseDTO buscarCep(String cep)
    {
        return viaCepFeignClient.buscarCep (cep);
    }
}