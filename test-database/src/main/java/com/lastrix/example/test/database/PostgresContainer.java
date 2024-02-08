package com.lastrix.example.test.database;

import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcConnectionDetails;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostgresContainer implements R2dbcConnectionDetails {
    PostgreSQLContainer<?> postgreSQLContainer;
    R2dbcProperties properties;

    public PostgresContainer(@NotNull DbTestContainerConfig config, @NotNull R2dbcProperties properties) {
        this.properties = properties;
        DockerImageName imageName = DockerImageName.parse(isBlank(config.getImage()) ? "postgres" : config.getImage())
                .asCompatibleSubstituteFor("postgres");
        String databaseName = isBlank(config.getName()) ? "postgres" : config.getName();
        String username = isBlank(properties.getUsername()) ? "user" : properties.getUsername();
        String password = isBlank(properties.getPassword()) ? UUID.randomUUID().toString() : properties.getPassword();
        postgreSQLContainer = new PostgreSQLContainer<>(imageName)
                .withImagePullPolicy(PullPolicy.alwaysPull())
                .withDatabaseName(databaseName)
                .withUsername(username)
                .withPassword(password)
                .withExposedPorts(5432);
    }

    @Override
    public ConnectionFactoryOptions getConnectionFactoryOptions() {
        String connectionUrl = getConnectionUrl();
        ConnectionFactoryOptions urlOptions = ConnectionFactoryOptions.parse(connectionUrl);
        ConnectionFactoryOptions.Builder optionsBuilder = urlOptions.mutate();
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.USER, postgreSQLContainer::getUsername, StringUtils::hasText);
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.PASSWORD, postgreSQLContainer::getPassword, StringUtils::hasText);
        configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.DATABASE, postgreSQLContainer::getDatabaseName, StringUtils::hasText);
        if (this.properties.getProperties() != null) {
            this.properties.getProperties()
                    .forEach((key, value) -> optionsBuilder.option(Option.valueOf(key), value));
        }
        log.debug("Configured r2dbc [url: {}]", connectionUrl);
        return optionsBuilder.build();
    }

    private <T extends CharSequence> void configureIf(ConnectionFactoryOptions.Builder optionsBuilder,
                                                      ConnectionFactoryOptions originalOptions, Option<T> option, Supplier<T> valueSupplier,
                                                      Predicate<T> setIf) {
        if (originalOptions.hasOption(option)) {
            return;
        }
        T value = valueSupplier.get();
        if (setIf.test(value)) {
            optionsBuilder.option(option, value);
        }
    }

    @PostConstruct
    public void start() {
        postgreSQLContainer.start();
    }

    @NonNull
    private String getConnectionUrl() {
        return "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName();
    }

    @PreDestroy
    public void stop() {
        postgreSQLContainer.stop();
    }

}
