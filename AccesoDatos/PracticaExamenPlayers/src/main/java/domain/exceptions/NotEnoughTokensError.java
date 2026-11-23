package domain.exceptions;

public class NotEnoughTokensError extends RuntimeException {
    public NotEnoughTokensError(String message) {
        super(message);
    }
}
