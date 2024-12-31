package com.app.NotesService.exception;

public class NoteNotFoundException extends RuntimeException{

    public NoteNotFoundException() {
    }

    public NoteNotFoundException(String message) {
        super(message);
    }
}
