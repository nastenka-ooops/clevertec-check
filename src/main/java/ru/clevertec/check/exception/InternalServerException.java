package main.java.ru.clevertec.check.exception;

public class InternalServerException extends CheckException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
