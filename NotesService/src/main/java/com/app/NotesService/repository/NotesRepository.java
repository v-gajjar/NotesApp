package com.app.NotesService.repository;

import com.app.NotesService.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
}