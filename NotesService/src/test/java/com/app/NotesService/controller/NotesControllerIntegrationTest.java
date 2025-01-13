package com.app.NotesService.controller;

import com.app.NotesService.model.ApiError;
import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import com.app.NotesService.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-integration-test.properties")
@Sql(scripts = {"/scripts/pre-test-setup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotesControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() throws Exception{
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes");

        Note note = new Note(null, "I love testing!", "I LOVE writing Integration Tests!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
        assertNotNull(returnedNote.getId());
    }

    @Test
    @Order(2)
    public void Save_NoteWithContentButWithoutTitle_ReturnsNoteWithAddedId() throws Exception{
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes");

        Note note = new Note(null, null, "I enjoy a mug of Java!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
        assertNotNull(returnedNote.getId());
    }

    @Test
    @Order(3)
    public void Save_NoteWithNullContent_ThrowsError() throws Exception {
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes");

        Note note = new Note(null, "I love testing!", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertEquals("Note cannot have empty content", message);

    }

    @Test
    @Order(4)
    public void Save_NoteWithEmptyContent_ThrowsError() throws Exception {
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes");

        Note note = new Note(null, "I love testing!", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertEquals("Note cannot have empty content", message);
    }

    @Test
    @Order(5)
    public void Find_ExistingNoteID_ReturnsASingleNote() throws Exception{
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes/1");

        Note note = new Note(1L, "I love testing!", "I LOVE writing Integration Tests!");
        String noteJson = objectMapper.writeValueAsString(note);

        // act
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

        String responseContent = result.getBody();
        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
        assertNotNull(returnedNote.getId());
        assertEquals(noteJson, responseContent);
    }

    @Test
    @Order(6)
    public void Find_IDWithoutRowInDatabase_ThrowsError() throws Exception{
        // arrange
        long id = 32L;
        URI uri = new URI("http://localhost:" + port + "/api/notes/" + id);

        // act
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
        assertEquals("No entry found in database for note with id: " + id, message);
    }

    @Test
    @Order(7)
    public void Find_IDWithNonNumericalValue_ThrowsError() throws Exception{
        // arrange
        String id="abc";
        URI uri = new URI("http://localhost:" + port + "/api/notes/" + id);

        // act
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertTrue(message.startsWith("Incorrect data type provided for id"));
    }

    @Test
    @Order(8)
    public void Update_ExistingNoteInDatabase_ReturnsUpdatedNote() throws Exception{
        // arrange
        Long id = 1L;
        URI uri = new URI("http://localhost:" + port + "/api/notes/" + id);

        Note existingNote = new Note(id, "I love testing!", "I LOVE writing Integration Tests!");
        Note updatedNote = new Note(id, "Updated Note Title", "Updated note contents");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(updatedNote, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
        assertEquals(updatedNote.toString(), returnedNote.toString());
    }

    @Test
    @Order(9)
    public void Update_NoteNotFoundInDatabase_ThrowsError() throws Exception{
        // arrange
        Long id = 32L;
        URI uri = new URI("http://localhost:" + port + "/api/notes/" + id);

        Note existingNote = new Note(id, "I love testing!", "I LOVE writing Integration Tests!");
        Note updatedNote = new Note(id, "Updated Note Title", "Updated note contents");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(updatedNote, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
        assertEquals("Unable to update provided note", message);
    }

    @Test
    @Order(10)
    public void Update_IDWithNonNumericalValue_ThrowsError() throws Exception {
        // arrange
        String id="abc";
        URI uri = new URI("http://localhost:" + port + "/api/notes/" + id);

        Note existingNote = new Note(1L, "I love testing!", "I LOVE writing Integration Tests!");
        Note updatedNote = new Note(1L, "Updated Note Title", "Updated note contents");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(updatedNote, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertTrue(message.startsWith("Incorrect data type provided for id"));

    }

    @Test
    @Order(11)
    public void Delete_ExistingNoteID_ReturnsAString() throws Exception{
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes/1");

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(""), String.class);

        String message = result.getBody();

        // assert
        assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
        assertEquals("Note successfully deleted", message);
    }

    @Test
    @Order(12)
    public void Delete_IDWithoutRowInDatabase_ThrowsError() throws Exception{
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes/32");

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(""), String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
        assertEquals("Unable to delete note from database", message);

    }

    @Test
    @Order(13)
    public void Delete_IDWithNonNumericalValue_ThrowsError() throws Exception {
        // arrange
        URI uri = new URI("http://localhost:" + port + "/api/notes/abc");

        // act
        ResponseEntity<String> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(""), String.class);

        ApiError error = objectMapper.readValue(result.getBody(), ApiError.class );
        String message = error.getMessage();

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertTrue(message.startsWith("Incorrect data type provided for id"));
    }
}
