package com.talentsense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TalentSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentSenseApplication.class, args);
    }
}
