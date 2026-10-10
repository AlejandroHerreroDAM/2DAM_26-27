package domain.exceptions;

public class ForeignKeyException extends RuntimeException {
    public ForeignKeyException() {
        super(message);
    }
}
