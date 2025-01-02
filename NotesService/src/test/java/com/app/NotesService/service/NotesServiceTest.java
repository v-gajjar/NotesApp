package com.app.NotesService.service;

import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.exception.NoteNotFoundException;
import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "/application-unit-test.properties")
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

    @Test
    public void Find_IDWithoutRowInDatabase_ThrowsError(){
        // arrange
        long id = 32L;

        when(notesRepository.findById(id)).thenReturn(Optional.empty());

        // act
        Assertions.assertThrows(NoteNotFoundException.class, () -> {
            notesService.findNoteById(id);
        });

        // assert
        verify(notesRepository, times(1)).findById(id);
    }

    @Test
    public void Delete_ExistingNoteID_ReturnsAString() {
        // arrange
        Long id = 1L;

        when(notesRepository.existsById(id)).thenReturn(true);
        doNothing().when(notesRepository).deleteById(id);

        // act
        String message = notesService.deleteNoteById(id);

        // assert
        verify(notesRepository, times(1)).existsById(id);
        verify(notesRepository, times(1)).deleteById(id);
        assertEquals("Note successfully deleted", message);
    }

    @Test
    public void Delete_IDWithoutRowInDatabase_ThrowsError(){
        // arrange
        Long id = 32L;

        when(notesRepository.existsById(id)).thenReturn(false);

        // act
        Assertions.assertThrows(NoteNotFoundException.class, () -> {
            notesService.deleteNoteById(id);
        });

        // assert
        verify(notesRepository, times(1)).existsById(id);
        verify(notesRepository, never()).deleteById(id);

    }

    @Test
    public void Update_ExistingNoteInDatabase_ReturnsUpdatedNote() throws Exception{
        // arrange
        Note updatedNote = new Note(1L, "Updated Note", "Hello, World!");

        when(notesRepository.existsById(updatedNote.getId())).thenReturn(true);
        when(notesRepository.save(updatedNote)).thenReturn(updatedNote);


        // act
        notesService.update(updatedNote);

        // assert
        verify(notesRepository, times(1)).existsById(updatedNote.getId());
        verify(notesRepository, times(1)).save(any(Note.class));

    }

    @Test
    public void Update_NoteNotFoundInDatabase_ThrowsError() throws Exception{
        // arrange
        Note updatedNote = new Note(32L, "Updated Note", "Hello, World!");

        when(notesRepository.existsById(updatedNote.getId())).thenReturn(false);

        // act
        Assertions.assertThrows(NoteNotFoundException.class, () -> {
            notesService.update(updatedNote);
        });

        // assert
        verify(notesRepository, times(1)).existsById(updatedNote.getId());
        verify(notesRepository, never()).save(updatedNote);
    }
}
