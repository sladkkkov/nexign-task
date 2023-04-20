package ru.sladkkov.common.exception;

public class TariffNotExistsException extends RuntimeException {
    public TariffNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TariffNotExistsException(String message) {
        super(message);
    }
}
