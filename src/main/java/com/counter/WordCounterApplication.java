package com.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for message processing Spring Boot application.
 */
@SpringBootApplication
public class WordCounterApplication {

    /**
     * Main method, to run the {@link SpringApplication Spring application}.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(WordCounterApplication.class, args);
    }
}
