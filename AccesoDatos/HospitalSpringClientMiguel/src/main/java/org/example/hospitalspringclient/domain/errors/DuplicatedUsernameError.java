package org.example.hospitalspringclient.domain.errors;

public class DuplicatedUsernameError extends RuntimeException {
    public DuplicatedUsernameError(String message) {
        super(message);
    }
}
