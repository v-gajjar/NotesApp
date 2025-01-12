package com.app.NotesService.controller;

import com.app.NotesService.model.ApiError;
import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.exception.NoteNotFoundException;
import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(NotesController.class);

    @PostMapping
    public ResponseEntity<Note> save(@RequestBody Note note) {

        Note createdNote = notesService.save(note);

        return new ResponseEntity<Note>( createdNote, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findNoteById(@PathVariable Long id){

        Note note;

        try{
            note = notesService.findNoteById(id);
        }
        catch(NoteNotFoundException exception){
            logger.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
        return new ResponseEntity<Note>( note, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note note){

        Note updatedNote;

        try{
            updatedNote = notesService.update(note);
        }
        catch(NoteNotFoundException exception){
            logger.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        return new ResponseEntity<Note>( updatedNote, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long id){

        String message;

        try {
            message = notesService.deleteNoteById(id);
        } catch (NoteNotFoundException exception) {
            logger.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @ExceptionHandler(EmptyContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleEmptyContentException(EmptyContentException exception){

        logger.error(exception.getMessage());

        ApiError errorResponse = new ApiError();

        String message = exception.getMessage();
        int statusCode = HttpStatus.BAD_REQUEST.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiError>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        logger.error(exception.getMessage());

        ApiError errorResponse = new ApiError();

        String argumentName = exception.getName();
        String type = exception.getRequiredType().getSimpleName();

        String message = "Incorrect data type provided for " + argumentName + ". It should be of type: " + type;
        int statusCode = HttpStatus.BAD_REQUEST.value();

        errorResponse.setMessage(message);
        errorResponse.setStatusCode(statusCode);

        return new ResponseEntity<ApiError>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

