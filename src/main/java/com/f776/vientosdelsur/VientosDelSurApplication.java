package com.f776.vientosdelsur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VientosDelSurApplication {

    public static void main(String[] args) {
        SpringApplication.run(VientosDelSurApplication.class, args);
    }

}
