package main.java.ru.clevertec.check.exception;

public class NotEnoughMoneyException extends CheckException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
