package com.programmers.smrtstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmRtStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmRtStoreApplication.class, args);
    }

}
