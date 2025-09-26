package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.ViaCepResponseDTO;

// Criação de um Service para uma API dia 26.09.25
@Service
public class ViaCepService 
{
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";
    
    public ViaCepResponseDTO buscarCep(String cep)
    {
        RestTemplate restTemplate = new RestTemplate();
        
        // Faz uma chamada GET para a API ViaCEP e converte a resposta em ViaCepResponseDTO
        return restTemplate.getForObject(VIA_CEP_URL, ViaCepResponseDTO.class, cep);
    }


}
