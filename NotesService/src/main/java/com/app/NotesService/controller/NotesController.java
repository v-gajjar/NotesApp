package com.app.NotesService.controller;

import com.app.NotesService.exception.ApiExceptionDetails;
import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.exception.ResourceNotFoundException;
import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping
    public ResponseEntity<Note> save(@RequestBody Note note) {

        Note createdNote;

        try{
            createdNote = notesService.save(note);
        }
        catch (EmptyContentException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return new ResponseEntity<Note>( createdNote, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findNoteById(@PathVariable Long id){

        Note note;

        try{
            note = notesService.findNoteById(id);
        }
        catch(ResourceNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
        return new ResponseEntity<Note>( note, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiExceptionDetails> handleException(MethodArgumentTypeMismatchException exception) {
        ApiExceptionDetails errorResponse = new ApiExceptionDetails();

        String argumentName = exception.getName();
        String type = exception.getRequiredType().getSimpleName();

        String message = "Incorrect data type provided for " + argumentName + ". It should be of type: " + type;
        int statusCode = HttpStatus.BAD_REQUEST.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiExceptionDetails>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

