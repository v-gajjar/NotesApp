package com.app.NotesService.repository;

import com.app.NotesService.model.Note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class NotesRepositoryTest {

    @Autowired
    private NotesRepository notesRepository;

    @Test
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() {

        // Arrange
        Note note = new Note(null, "Sample Note", "Hello, World!");

        // Act
        Note savedNote = notesRepository.save(note);

        // Assert
        assertThat(savedNote).isNotNull();
        assertThat(savedNote.getId()).isNotNull();
        assertThat(savedNote.getId()).isGreaterThan(0);
    }
}
