package com.test.helmes.errors;

/**
 * Error response to be sent to the front-end when something is wrong.
 */
public class Error extends Exception {

    private String violationMessage;

    private String message;

    public Error( String message) {
        this.message = message;
    }
    public Error(String violationMessage, String message) {
        this.violationMessage = violationMessage;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
