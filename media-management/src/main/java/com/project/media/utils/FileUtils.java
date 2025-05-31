package com.project.media.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Slf4j
public class FileUtils {

    public static byte[] readLocalFile(String mediaBasePath,  String reference) {
        final String fileUploadFullPath = mediaBasePath + separator + reference;
        if (fileUploadFullPath.isEmpty()) return new byte[0];
        try {
            Path file = new File(fileUploadFullPath).toPath();
            return Files.readAllBytes(file);
        } catch (Exception ex) {
            log.error("No file found at {}", fileUploadFullPath, ex);
        }
        return new byte[0];
    }

    public static String saveLocalFile(MultipartFile file, String mediaBasePath, String relativePath) {
        String fileName = file.getOriginalFilename();
        String extension = extractFileExtension(fileName);
        final String reference = relativePath + separator + "whatsapp-clone-" + currentTimeMillis() + extension;
        final String fileUploadFullPath = mediaBasePath + separator + reference;
        Path targetPath = Paths.get(fileUploadFullPath);
        File uploadDir = targetPath.getParent().toFile();
        if (!uploadDir.exists()) {
            boolean folderCreated = uploadDir.mkdirs();
            if (!folderCreated) {
                log.error("Error creating upload directory");
                return null;
            }
        }
        try {
            Files.write(targetPath, file.getBytes());
            log.info("Successfully save media to " + targetPath);
        } catch (Exception ex) {
            log.error("File wasn't saved", ex);
        }
        return reference;
    }

    private static String extractFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

}
