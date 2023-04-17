package ru.sladkkov.dtoshare.exception;

public class AbonentNotFoundException extends RuntimeException {
    public AbonentNotFoundException() {
        super();
    }

    public AbonentNotFoundException(String message) {
        super(message);
    }

    public AbonentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
