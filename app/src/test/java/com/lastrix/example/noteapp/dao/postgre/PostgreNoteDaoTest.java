package com.lastrix.example.noteapp.dao.postgre;

import com.lastrix.example.noteapp.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostgreNoteDaoTest {
    @Autowired
    PostgreNoteDao dao;

    @Test
    void test_save_read() {
        StepVerifier.create(dao.save(Note.builder()
                        .title("Example")
                        .content("Hello")
                        .build()))
                .expectSubscription()
                .assertNext(note -> {
                    assertEquals(1L, note.getId());
                    assertNotNull(note.getCreatedAt());
                    assertNotNull(note.getUpdatedAt());
                    assertEquals(note.getCreatedAt(), note.getUpdatedAt());
                })
                .verifyComplete();

        StepVerifier.create(dao.find(1L))
                .expectSubscription()
                .assertNext(note -> {
                    assertEquals(1L, note.getId());
                    assertEquals("Example", note.getTitle());
                    assertEquals("Hello", note.getContent());
                    assertNotNull(note.getCreatedAt());
                    assertNotNull(note.getUpdatedAt());
                    assertEquals(note.getCreatedAt(), note.getUpdatedAt());
                })
                .verifyComplete();
    }
}
