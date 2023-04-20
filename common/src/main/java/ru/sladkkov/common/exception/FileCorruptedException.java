package ru.sladkkov.common.exception;

public class FileCorruptedException extends RuntimeException {
    public FileCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}

