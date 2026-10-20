package org.example.hospitalspringclient.domain.errors;

public class AppError extends RuntimeException {
    public AppError(String message) {
        super(message);
    }
}
