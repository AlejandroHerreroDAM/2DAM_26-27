package org.hospitalspringclient.domain.exceptions;

public class ForeignKeyException extends RuntimeException {
    public ForeignKeyException() {
        super("The record is referenced by another entity");
    }
}
