package com.app.NotesService.controller;

import com.app.NotesService.model.Note;
import com.app.NotesService.repository.NotesRepository;
import com.app.NotesService.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private String baseURL;

    @BeforeAll
    public void setUp(){
        baseURL = "http://localhost:" + port + "/api/notes";
    }

    @Test
    @Sql(statements = "DELETE FROM note;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() throws Exception{
        // arrange
        URI uri = new URI(baseURL);

        Note note = new Note(null, "I love testing!", "I LOVE writing Integration Tests!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        Assertions.assertEquals(201, result.getStatusCode().value());
        Assertions.assertNotNull(returnedNote.getId());
    }

    @Test
    @Sql(statements = "DELETE FROM note;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void Save_NoteWithContentButWithoutTitle_ReturnsNoteWithAddedId() throws Exception{
        // arrange
        URI uri = new URI(baseURL);

        Note note = new Note(null, null, "I enjoy a mug of Java!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        Note returnedNote = objectMapper.readValue(result.getBody(), Note.class);

        // assert
        Assertions.assertEquals(201, result.getStatusCode().value());
        Assertions.assertNotNull(returnedNote.getId());
    }

    @Test
    public void Save_NoteWithNullContent_ThrowsError() throws Exception {
        // arrange
        URI uri = new URI(baseURL);

        Note note = new Note(null, "I love testing!", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        // assert
        Assertions.assertEquals(400, result.getStatusCode().value());
    }

    @Test
    public void Save_NoteWithEmptyContent_ThrowsError() throws Exception {
        // arrange
        URI uri = new URI(baseURL);

        Note note = new Note(null, "I love testing!", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<Note> request = new HttpEntity<>(note, headers);

        // act
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        // assert
        Assertions.assertEquals(400, result.getStatusCode().value());
    }
}
