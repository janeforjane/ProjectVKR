package logic.exception;

public class DuplicateFIOException extends Exception {

    public DuplicateFIOException(String message) {
        super(message);
    }

    public DuplicateFIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
