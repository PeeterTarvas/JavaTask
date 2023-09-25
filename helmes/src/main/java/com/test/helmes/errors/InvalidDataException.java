package com.test.helmes.errors;

/**
 * Exception throws when data is invalid.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
