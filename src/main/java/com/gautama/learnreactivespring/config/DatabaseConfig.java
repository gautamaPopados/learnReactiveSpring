package com.gautama.learnreactivespring.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy;

import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@EnableConfigurationProperties
public class DatabaseConfig {

    @Bean("postgresConnectionFactory")
    public ConnectionFactory postgresConnectionFactory(
            @Value("${app.r2dbc.postgres.url}") String url,
            @Value("${app.r2dbc.postgres.username}") String username,
            @Value("${app.r2dbc.postgres.password}") String password) {

        return ConnectionFactories.get(
                ConnectionFactoryOptions.parse(url)
                        .mutate()
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .build()
        );
    }

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

    @Bean(name = "postgresTemplate")
    public R2dbcEntityTemplate postgresTemplate(@Qualifier("postgresConnectionFactory") ConnectionFactory cf) {
        return new R2dbcEntityTemplate(new TransactionAwareConnectionFactoryProxy(cf));
    }

    @Bean(name = "mysqlTemplate")
    public R2dbcEntityTemplate mysqlTemplate(@Qualifier("mysqlConnectionFactory") ConnectionFactory cf) {
        return new R2dbcEntityTemplate(new TransactionAwareConnectionFactoryProxy(cf));
    }
}