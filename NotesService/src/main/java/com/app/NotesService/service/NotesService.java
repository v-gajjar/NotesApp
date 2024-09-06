package com.app.NotesService.service;

import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    public Note save(Note note){
        return notesRepository.save(note);
    }
}
