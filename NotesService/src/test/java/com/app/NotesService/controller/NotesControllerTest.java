package com.app.NotesService.controller;

import com.app.NotesService.model.ApiError;
import com.app.NotesService.exception.EmptyContentException;
import com.app.NotesService.exception.NoteNotFoundException;
import com.app.NotesService.model.Note;
import com.app.NotesService.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotesController.class)
@TestPropertySource(locations = "/application-unit-test.properties")
public class NotesControllerTest {

    @MockBean
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Save_NoteWithTitleAndContent_ReturnsNoteWithAddedId() throws Exception{
        // arrange
        Note noteToCreate = new Note(null, "Sample Note", "Hello, World!");
        Note createdNote = new Note(1L, "Sample Note", "Hello, World!");

        when(notesService.save(any(Note.class))).thenReturn(createdNote);

        String initialNoteJson = objectMapper.writeValueAsString(noteToCreate);
        String createdNoteJson = objectMapper.writeValueAsString(createdNote);

        // act
        MvcResult result = mockMvc.perform( post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initialNoteJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // assert
        String response = result.getResponse().getContentAsString();
        assertThat(response).isNotEmpty();
        assertThat(response).isEqualTo(createdNoteJson);
    }

    @Test
    public void Save_NoteWithNullContent_ThrowsError() throws Exception{
        // arrange
        Note noteToCreate = new Note(null, "Sample Note", null);
        EmptyContentException exception = new EmptyContentException("Note cannot have empty content");

        when(notesService.save(any(Note.class))).thenThrow(exception);

        String initialNoteJson = objectMapper.writeValueAsString(noteToCreate);

        // act
        MvcResult result = mockMvc.perform( post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialNoteJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        // assert
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void Find_ExistingNoteID_ReturnsASingleNote() throws Exception{
        //arrange
        Note existingNote = new Note(1L, "Sample Note", "Hello, World!");
        String existingNoteJSON = objectMapper.writeValueAsString(existingNote);

        when(notesService.findNoteById(any(Long.class))).thenReturn(existingNote);

        // act
        MvcResult result = mockMvc.perform(get("/api/notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // assert
        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isNotEmpty();
        assertThat(existingNoteJSON).isEqualTo(responseContent);
    }

    @Test
    public void Find_IDWithoutRowInDatabase_ThrowsError() throws Exception{
        //arrange
        long id = 32L;

        NoteNotFoundException exception = new
                NoteNotFoundException("No entry found in database for note with id: " + id);
        when(notesService.findNoteById(any(Long.class))).thenThrow(exception);

        // act
        MvcResult result = mockMvc.perform(get("/api/notes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        // assert
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void Find_IDWithNonNumericalValue_ThrowsError() throws Exception{
        // arrange
        String id="abc";

        // act
        MvcResult result = mockMvc.perform(get("/api/notes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        // assert
        MockHttpServletResponse response = result.getResponse();

        String content = response.getContentAsString();
        ApiError error = objectMapper.readValue(content, ApiError.class );

        String message = error.getMessage();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(message.startsWith("Incorrect data type provided for id"));
    }

    @Test
    public void Delete_ExistingNoteID_ReturnsAString() throws Exception{
        //arrange
        Note existingNote = new Note(1L, "Sample Note", "Hello, World!");

        when(notesService.deleteNoteById(any(Long.class))).thenReturn("Note successfully deleted");

        // act
        MvcResult result = mockMvc.perform(delete("/api/notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // assert
        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isNotEmpty();
        assertThat(responseContent).isEqualTo("Note successfully deleted");
    }

    @Test
    public void Delete_IDWithoutRowInDatabase_ThrowsError() throws Exception{
        //arrange
        long id = 32L;

        NoteNotFoundException exception = new
                NoteNotFoundException("Unable to delete note from database");

        when(notesService.deleteNoteById(any(Long.class))).thenThrow(exception);

        // act
        MvcResult result = mockMvc.perform(delete("/api/notes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        // assert
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void Delete_IDWithNonNumericalValue_ThrowsError() throws Exception {
        // arrange
        String id="abc";

        // act
        MvcResult result = mockMvc.perform(delete("/api/notes/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        // assert
        MockHttpServletResponse response = result.getResponse();

        String content = response.getContentAsString();
        ApiError error = objectMapper.readValue(content, ApiError.class );

        String message = error.getMessage();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(message.startsWith("Incorrect data type provided for id"));
    }

    @Test
    public void Update_ExistingNoteInDatabase_ReturnsUpdatedNote() throws Exception{
        // arrange
        Note updatedNote = new Note(1L, "Updated Note", "Hello, World!");

        when(notesService.update(any(Note.class))).thenReturn(updatedNote);

        String updatedNoteJson = objectMapper.writeValueAsString(updatedNote);

        // act
        MvcResult result = mockMvc.perform( put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedNoteJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // assert
        String response = result.getResponse().getContentAsString();
        assertThat(response).isNotEmpty();
        assertThat(response).isEqualTo(updatedNoteJson);

        verify(notesService, times(1)).update(any(Note.class));
    }
}
