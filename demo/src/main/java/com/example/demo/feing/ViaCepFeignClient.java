package com.example.demo.feing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.ViaCepResponseDTO;

import config.FeignConfig;

// Mudança para ErroDecoder 
@FeignClient (name = "viacep", url = "https://viacep.com.br/ws", configuration = FeignConfig.class)
public interface ViaCepFeignClient 
{

    @GetMapping("/{cep}/json/")
    ViaCepResponseDTO buscarCep(@PathVariable("cep") String cep);

    
}
