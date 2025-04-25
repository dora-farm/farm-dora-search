package com.farmdora.farmdora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FarmdoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmdoraApplication.class, args);
    }

}
