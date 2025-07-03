package com.project.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableReactiveMongoAuditing(auditorAwareRef = "auditAwareImpl")
public class MediaManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaManagementApplication.class, args);
    }

}
