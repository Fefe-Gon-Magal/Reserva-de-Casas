package config;

import org.springframework.context.annotation.Configuration;

import com.example.exceptions.ViaCepServiceException;

import feign.Response;
import feign.codec.ErrorDecoder;


    
@Configuration
public class CustomErrorDecoder implements ErrorDecoder 
{

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) 
    {
        if (response.status() >= 400 && response.status() < 500) 
        {
            // Erros do cliente (e.g., 400 Bad Request, 404 Not Found)
            return new ViaCepServiceException("Erro do cliente ao consultar ViaCep: " + response.reason(), response.status());
        } 
        
        else if (response.status() >= 500 && response.status() < 600) 
        {
            // Erros do servidor (e.g., 500 Internal Server Error, 503 Service Unavailable)
            return new ViaCepServiceException("Erro interno do ViaCep: " + response.reason(), response.status());
        }
        
        // Para outros erros, ou se não quisermos tratar especificamente, usamos o decodificador padrão
        return defaultErrorDecoder.decode(methodKey, response);
    }
}


