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
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() throws Exception{
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
}
