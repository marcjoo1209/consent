package com.ctp.consent.exception;

public class ConsentNotFoundException extends RuntimeException {

    public ConsentNotFoundException(String message) {
        super(message);
    }

    public ConsentNotFoundException(Long id) {
        super("Consent not found with ID: " + id);
    }
}