package com.lastrix.example.noteapp.dao;

import com.lastrix.example.noteapp.model.Note;
import reactor.core.publisher.Mono;

public interface NoteDao {
    Mono<Note> save(Note template);

    Mono<Note> find(Long id);
}
