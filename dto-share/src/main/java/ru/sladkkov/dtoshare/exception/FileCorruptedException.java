package ru.sladkkov.dtoshare.exception;

public class FileCorruptedException extends RuntimeException {
    public FileCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}

