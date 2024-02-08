package com.lastrix.example.test.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@SpringBootTest
public class ExampleTest {
    @Autowired
    DatabaseClient client;

    @Test
    void test1() {
        StepVerifier.create(
                        client.sql("SELECT 1")
                                .map((row, metadata) -> row.get(0, Integer.class))
                                .one()
                ).expectSubscription()
                .expectNext(1)
                .verifyComplete();
    }
}
