package com.andeva.atelier.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AtelierPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtelierPlatformApplication.class, args);
    }

}
