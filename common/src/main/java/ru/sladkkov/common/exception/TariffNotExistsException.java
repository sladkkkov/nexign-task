package ru.sladkkov.common.exception;

public class TariffNotExistsException extends RuntimeException {

    public TariffNotExistsException(String message) {
        super(message);
    }
}
