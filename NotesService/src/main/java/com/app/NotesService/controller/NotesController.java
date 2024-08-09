package com.app.NotesService.controller;

import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping
    public ResponseEntity<Note> save(@RequestBody Note note){

        Note createdNote = notesService.save(note);

        return new ResponseEntity<Note>(createdNote, HttpStatus.CREATED);
    }
}
