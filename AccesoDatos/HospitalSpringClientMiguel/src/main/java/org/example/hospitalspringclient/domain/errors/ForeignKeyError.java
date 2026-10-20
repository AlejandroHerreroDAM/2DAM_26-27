package org.example.hospitalspringclient.domain.errors;

public class ForeignKeyError extends RuntimeException {
    public ForeignKeyError(String message) {
        super(message);
    }
}
