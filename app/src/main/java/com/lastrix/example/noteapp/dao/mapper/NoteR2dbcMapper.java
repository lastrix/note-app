package com.lastrix.example.noteapp.dao.mapper;

import com.lastrix.example.noteapp.model.Note;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class NoteR2dbcMapper implements R2dbcMapper<Note> {
    @Override
    public Note apply(Row row, RowMetadata metadata) {
        return Note.builder()
                .id(row.get("id", Long.class))
                .title(row.get("title", String.class))
                .content(row.get("content", String.class))
                .createdAt(row.get("created_at", OffsetDateTime.class))
                .updatedAt(row.get("updated_at", OffsetDateTime.class))
                .build();
    }
}
