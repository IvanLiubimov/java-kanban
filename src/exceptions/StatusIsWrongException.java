package exceptions;

public class StatusIsWrongException extends RuntimeException {
    public StatusIsWrongException(String message) {
        super(message);
    }
}
