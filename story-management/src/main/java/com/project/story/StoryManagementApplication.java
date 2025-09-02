package com.project.story;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.project.*")
@EnableFeignClients
@EnableMongoAuditing(auditorAwareRef = "auditAwareImpl")
public class StoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryManagementApplication.class, args);
    }

}
