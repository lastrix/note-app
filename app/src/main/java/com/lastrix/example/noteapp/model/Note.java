package com.lastrix.example.noteapp.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class Note {
    Long id;
    @NonNull
    String title;
    String content;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
