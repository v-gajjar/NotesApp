package com.app.NotesService.exception;

public class EmptyContentException extends RuntimeException{

    public EmptyContentException() {
    }

    public EmptyContentException(String message) {
        super(message);
    }
}
