package com.app.NotesService.service;

import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    public Note save(Note note){
        String content = note.getContent();

        if ( content == null || content.trim().isEmpty() ){
            throw new EmptyContentException("Note cannot have empty content");
        }

        return notesRepository.save(note);
    }

    public Note findNoteById(Long noteID){
        return new Note(1L, "Sample Note", "Hello, World!");
    }
}
