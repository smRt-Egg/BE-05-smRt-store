package com.programmers.smrtstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SmRtStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmRtStoreApplication.class, args);
    }

}
