package exceptions;

public class FileManagerFileSaveException extends RuntimeException {
    public FileManagerFileSaveException(String message) {
        super(message);
    }
}
