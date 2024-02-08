package com.lastrix.example.noteapp.dao.postgre;

import com.lastrix.example.noteapp.dao.NoteDao;
import com.lastrix.example.noteapp.dao.mapper.R2dbcMapper;
import com.lastrix.example.noteapp.model.Note;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostgreNoteDao implements NoteDao {
    DatabaseClient client;
    R2dbcMapper<Note> mapper;

    public static final String CREATE_NOTE = """
            INSERT INTO note_tab(title, content)
            VALUES (:title, :content)
            RETURNING *
            """;

    @Override
    public Mono<Note> save(Note template) {
        return client.sql(CREATE_NOTE)
                .bind("title", template.getTitle())
                .bind("content", defaultString(template.getContent()))
                .map(mapper)
                .one();
    }

    public static final String FIND_NOTE = """
            SELECT * FROM note_tab WHERE id = :id
            """;

    @Override
    public Mono<Note> find(Long id) {
        return client.sql(FIND_NOTE)
                .bind("id", id)
                .map(mapper)
                .one();
    }
}
