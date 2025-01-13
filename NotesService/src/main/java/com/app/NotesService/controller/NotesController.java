package com.app.NotesService.controller;

import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Note note = notesService.findNoteById(id);

        return new ResponseEntity<Note>( note, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note note){

        Note updatedNote = notesService.update(note);

        return new ResponseEntity<Note>( updatedNote, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long id){

        String message = notesService.deleteNoteById(id);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
}

