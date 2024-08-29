package com.app.NotesService.controller;

import com.app.NotesService.exception.EmptyContentException;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NotesController.class)
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

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
