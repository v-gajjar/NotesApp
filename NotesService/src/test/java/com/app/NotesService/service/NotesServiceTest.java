package com.app.NotesService.service;

import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    @Test
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() {
        // arrange
        Note noteToSave = new Note(null, "Sample Note", "Java > JavaScript > C#");
        Note savedNote = new Note(2L,"Sample Note", "Java > JavaScript > C#" );

        when(notesRepository.save(noteToSave)).thenReturn(savedNote);

        // act
        Note result = notesService.save(noteToSave);

        // assert
        assertNotNull(result);
        assertEquals(savedNote.getId(), result.getId());
    }

    @Test
    public void Save_NoteWithEmptyContent_ThrowsError(){
        Note noteToSave = new Note(null, "Sample Note", "");

        Assertions.assertThrows(EmptyContentException.class, () -> {
            notesService.save(noteToSave);
        });

        verify(notesRepository, never()).save(any(Note.class));
    }

    @Test
    public void Save_NoteWithNullContent_ThrowsError(){
        Note noteToSave = new Note(null, "Sample Note", null);

        Assertions.assertThrows(EmptyContentException.class, () -> {
            notesService.save(noteToSave);
        });

        verify(notesRepository, never()).save(any(Note.class));
    }

    @Test
    public void Find_ExistingNoteID_ReturnsASingleNote() {
        // arrange
        Note existingNote = new Note(1L, "Sample Note", "Hello, World!");

        when(notesRepository.findById(1L)).thenReturn(Optional.of(existingNote));

        // act
        Note foundNote =  notesService.findNoteById(1L);

        // assert
        assertNotNull(foundNote);
        assertEquals(existingNote.getId(), foundNote.getId());
        assertEquals(existingNote.getTitle(), foundNote.getTitle());
        assertEquals(existingNote.getContent(), foundNote.getContent());
    }
}
