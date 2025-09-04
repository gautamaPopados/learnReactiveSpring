package com.gautama.learnreactivespring.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.gautama.learnreactivespring.mysql.repository",
        entityOperationsRef = "mysqlEntityTemplate"
)
public class MysqlConfig {
    @Bean("mysqlConnectionFactory")
    public ConnectionFactory mysqlConnectionFactory(
            @Value("${app.r2dbc.mysql.url}") String url,
            @Value("${app.r2dbc.mysql.username}") String username,
            @Value("${app.r2dbc.mysql.password}") String password) {

        return ConnectionFactories.get(
                ConnectionFactoryOptions.parse(url)
                        .mutate()
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .build()
        );
    }

    @Bean
    @Qualifier("mysqlEntityTemplate")
    public R2dbcEntityOperations mysqlEntityTemplate(
            @Qualifier("mysqlConnectionFactory") ConnectionFactory connectionFactory) {
        final DefaultReactiveDataAccessStrategy strategy =
                new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
        final DatabaseClient databaseClient = DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
                .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }
}