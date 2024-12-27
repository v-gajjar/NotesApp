package com.app.NotesService.repository;

import com.app.NotesService.model.Note;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "/application-unit-test.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class NotesRepositoryTest {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TestEntityManager entityManager;

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

    @Test
    public void Find_ExistingNoteID_ReturnsASingleNote() {
        Note note = new Note(null, "Sample Note", "Hello, World!");
        entityManager.persist(note);

        Note foundNote = notesRepository.findById(1L).orElse(null);

        // assert
        assertNotNull(foundNote);
        assertEquals(note.getId(), foundNote.getId());
        assertEquals(note.getTitle(), foundNote.getTitle());
        assertEquals(note.getContent(), foundNote.getContent());
    }

    @Test
    public void Find_IDWithoutRowInDatabase_ThrowsError(){
        Note note = notesRepository.findById(32L).orElse(null);

        assertNull(note);
    }
}
