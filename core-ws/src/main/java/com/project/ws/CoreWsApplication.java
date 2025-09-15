package com.project.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.project.*")
public class CoreWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreWsApplication.class, args);
    }

}
