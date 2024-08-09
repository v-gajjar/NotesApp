package com.app.NotesService.service;

import com.app.NotesService.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

    public Note save(Note note){
        return new Note(1L, "Sample Note", "Hello, World!");
    }
}
