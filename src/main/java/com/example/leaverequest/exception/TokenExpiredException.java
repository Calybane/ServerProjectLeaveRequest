package com.example.leaverequest.exception;

public class TokenExpiredException extends RuntimeException
{
    public TokenExpiredException(String message)
    {
        super(message);
    }
}