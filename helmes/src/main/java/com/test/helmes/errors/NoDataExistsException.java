package com.test.helmes.errors;

public class NoDataExistsException extends RuntimeException {
    public NoDataExistsException(String message) {
        super(message);
    }
}