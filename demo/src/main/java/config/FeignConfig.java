package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class FeingConfig 
{
    @Bean
    public ErrorDecoder errorDecoder() 
    {
        return new CustomErrorDecoder();
    }

}
