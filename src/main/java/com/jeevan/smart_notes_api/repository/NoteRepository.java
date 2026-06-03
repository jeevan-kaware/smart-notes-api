package com.jeevan.smart_notes_api.repository;

import com.jeevan.smart_notes_api.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByTitleContainingIgnoreCase(String title);
    Optional<Note> findByIdAndUserUsername(Long id, String username);
    List<Note> findByUserUsername(String username);
    Page<Note> findByUserUsername(
            String username,
            Pageable pageable
    );

    List<Note> findByUserUsername(
            String username,
            Sort sort
    );
}