package com.project.media.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "application.file.uploads.media-storage-type", havingValue = "cloudinary")
public class CloudinaryConfig {
    @Bean
    public Cloudinary configureCloudinary() {
        Dotenv dotenv = Dotenv.load();
        return new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }
}
