package Generic;

public class UnexpectedJsonFormatException extends RuntimeException {
    public UnexpectedJsonFormatException(String message) {
        super(message);
    }
}
