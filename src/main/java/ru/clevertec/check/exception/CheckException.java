package ru.clevertec.check.exception;

public class CheckException extends RuntimeException {
    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
