package com.finologie.banking.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class WebBankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebBankingApiApplication.class, args);
    }

}
