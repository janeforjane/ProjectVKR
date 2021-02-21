package logic.exception;

public class VacationHaveDateException extends Exception {

    public VacationHaveDateException(String message) {
        super(message);
    }

    public VacationHaveDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
