package com.reviewping.coflo.repository;

public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
