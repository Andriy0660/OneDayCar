package com.example.onedaycar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class OneDayCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneDayCarApplication.class, args);
    }

}
