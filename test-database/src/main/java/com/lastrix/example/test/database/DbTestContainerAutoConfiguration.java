package com.lastrix.example.test.database;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static java.beans.Introspector.decapitalize;

@Slf4j
@ConditionalOnProperty(name = "testcontainers.db.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties({R2dbcProperties.class, DbTestContainerConfig.class})
public class DbTestContainerAutoConfiguration implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.getBeanDefinition(decapitalize(ConnectionFactory.class.getSimpleName()))
                .setDependsOn(decapitalize(PostgresContainer.class.getSimpleName()));
    }

    @Bean
    public PostgresContainer postgresContainer(
            DbTestContainerConfig dbTestContainerConfig,
            R2dbcProperties r2dbcProperties
    ) {
        return new PostgresContainer(dbTestContainerConfig, r2dbcProperties);
    }
}
