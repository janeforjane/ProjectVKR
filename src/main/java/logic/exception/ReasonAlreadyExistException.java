package logic.exception;

public class ReasonAlreadyExistException extends Exception {

    public ReasonAlreadyExistException(String message) {
        super(message);
    }

    public ReasonAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
