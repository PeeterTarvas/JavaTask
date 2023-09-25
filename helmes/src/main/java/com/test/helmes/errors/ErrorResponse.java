package com.test.helmes.errors;

/**
 * Error response to be sent to the front-end when something is wrong.
 */
public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
