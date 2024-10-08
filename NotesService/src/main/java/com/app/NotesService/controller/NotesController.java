package com.app.NotesService.controller;

import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Note> find(@PathVariable Long id){
        Note foundNote = notesService.findNoteById(id);

        return new ResponseEntity<>( foundNote, HttpStatus.OK);
    }
}

