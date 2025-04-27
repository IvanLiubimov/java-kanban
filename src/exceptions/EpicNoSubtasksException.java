package exceptions;

public class EpicNoSubtasksException extends RuntimeException {
    public EpicNoSubtasksException(String message) {
        super(message);
    }
}
