package com.app.NotesService.exception;

public class ApiExceptionDetails {

    private String message;
    private int statusCode;

    public ApiExceptionDetails() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
