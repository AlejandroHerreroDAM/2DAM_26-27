package org.example.hospitalspringclient.domain.errors;

public class DatabaseError extends RuntimeException {

    public DatabaseError(String message) {
        super(message);
    }



}
