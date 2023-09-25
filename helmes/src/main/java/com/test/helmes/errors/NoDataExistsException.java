package com.test.helmes.errors;

/**
 * Exception throws when data is is missing.
 */
public class NoDataExistsException extends RuntimeException {
    public NoDataExistsException(String message) {
        super(message);
    }
}