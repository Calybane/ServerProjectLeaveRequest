package com.example.leaverequest.exception;

/**
 * Created by admin on 19/07/2017.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
