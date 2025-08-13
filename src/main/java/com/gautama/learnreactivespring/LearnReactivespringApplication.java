package com.gautama.learnreactivespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LearnReactivespringApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnReactivespringApplication.class, args);
    }

}
