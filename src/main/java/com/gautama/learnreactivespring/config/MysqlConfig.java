package com.gautama.learnreactivespring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.gautama.learnreactivespring.mysql.repository", // пакет с CustomerRepository
        entityOperationsRef = "mysqlTemplate"
)
public class MysqlConfig {}
