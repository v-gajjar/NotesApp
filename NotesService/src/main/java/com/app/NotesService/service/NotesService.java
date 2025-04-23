package com.app.NotesService.service;

import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.exception.NoteNotFoundException;
import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        return notesRepository.findById(noteID)
                .orElseThrow(()-> new NoteNotFoundException(
                        "No entry found in database for note with id: " + noteID
                ));
    }

    public String deleteNoteById(Long noteID) {

        if ( ! notesRepository.existsById(noteID) ){
            throw new NoteNotFoundException(
                    "Unable to delete note from database"
            );
        }

        notesRepository.deleteById(noteID);

        return "Note successfully deleted";
    }

    public Note update(Note note){

        if ( ! notesRepository.existsById(note.getId())) {
            throw new NoteNotFoundException(
                    "Unable to update provided note"
            );
        }
        return notesRepository.save(note);
    }

    public List<Note> findNotesByUserId(Long userId){
        /*
            This is a temporary stub implementation which will be replaced
            with actual logic after writing first writing the related service unit test
            (I'm using Test-Driven Development where possible)
         */
        return new ArrayList<Note>();
    }
}
