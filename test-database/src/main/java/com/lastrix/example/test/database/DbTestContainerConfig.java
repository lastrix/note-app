package com.lastrix.example.test.database;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("testcontainers.db")
public class DbTestContainerConfig {
    Boolean enabled = true;
    @NotNull
    String image;
    String name = "postgres";
}
