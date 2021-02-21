package logic.exception;

public class DateIsBusyException extends Exception {

    public DateIsBusyException(String message) {
        super(message);
    }

    public DateIsBusyException(String message, Throwable cause) {
        super(message, cause);
    }
}
