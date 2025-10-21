package com.example.exceptions;


public class ViaCepServiceException extends RuntimeException

{
    
private final int status;

public ViaCepServiceException( String message, int status)
{
    super (message);
    this.status = status;
}
    public int getStatus()
    {
        return status;
    }
}
