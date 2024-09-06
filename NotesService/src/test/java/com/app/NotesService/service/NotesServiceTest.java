package com.app.NotesService.service;

import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    @Test
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() throws Exception{
        Note noteToSave = new Note(null, "Sample Note", "Java > JavaScript > C#");
        Note savedNote = new Note(2L,"Sample Note", "Java > JavaScript > c#" );

        when(notesRepository.save(noteToSave)).thenReturn(savedNote);

        Note result = notesService.save(noteToSave);

        assertNotNull(result);
        assertEquals(savedNote.getId(), result.getId());
    }

}
