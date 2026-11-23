package domain.exceptions;

public class InsufficientTokensError extends RuntimeException {
    public InsufficientTokensError(String message) {
        super(message);
    }
}
