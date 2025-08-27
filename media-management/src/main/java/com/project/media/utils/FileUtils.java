package com.project.media.utils;

import com.project.media.exceptions.FileCreationException;
import com.project.media.exceptions.FileDeletionException;
import com.project.media.exceptions.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Slf4j
public class FileUtils {

    public static Resource getLocalFileResource(
        String mediaBasePath, String reference, String fileName
    ) {
        final String fileUploadFullPath = mediaBasePath + separator + reference + separator + fileName;
        try {
            Path file = new File(fileUploadFullPath).toPath();
            log.info("File found at {}", file.getFileName());
            return new UrlResource(file.toUri());
        } catch (Exception ex) {
            log.error("No file found at {}", fileUploadFullPath, ex);
            throw new FileNotFoundException("No file found at " + fileUploadFullPath, ex);
        }
    }

    public static byte[] readLocalFile(String mediaBasePath,  String reference, String fileName) {
        final String fileUploadFullPath = mediaBasePath + separator + reference + separator + fileName;
        if (fileUploadFullPath.isEmpty()) return new byte[0];
        try {
            Path file = new File(fileUploadFullPath).toPath();
            return Files.readAllBytes(file);
        } catch (Exception ex) {
            log.error("No file found at {}", fileUploadFullPath, ex);
            throw new FileNotFoundException("No file found at " + fileUploadFullPath, ex);
        }
    }

    public static Mono<String> saveLocalFile(FilePart filePart, String mediaBasePath, String relativePath) {
        String originalFilename = filePart.filename();
        String extension = extractFileExtension(originalFilename);
        String fileName = "whatsapp-clone-" + System.currentTimeMillis() + '.' + extension;

        String fileUploadFullPath = mediaBasePath + separator + relativePath + separator + fileName;
        Path targetPath = Paths.get(fileUploadFullPath);
        File uploadDir = targetPath.getParent().toFile();

        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            log.error("Error creating upload directory");
            return Mono.error(new FileCreationException("Could not create upload directory"));
        }

        return filePart.transferTo(targetPath)
            .then(Mono.fromCallable(() -> {
                log.info("Successfully saved media to: {}", targetPath);
                return fileName;
            }))
            .onErrorResume(ex -> {
                log.error("File not saved", ex);
                return Mono.error(ex);
            });
    }


    public static boolean deleteLocalFile(String mediaBasePath, String reference, String fileName) {
        final String fileFullPath = mediaBasePath + separator + reference + separator + fileName;

        if (fileFullPath.isEmpty()) return false;
        try {
            File file = new File(fileFullPath);
            log.info("Deleting file: {}", file.getTotalSpace());
            return file.delete();
        } catch (Exception ex) {
            log.error("Error deleting file at {}", fileFullPath, ex);
            throw new FileDeletionException("File not deleted", ex);
        }
    }

    public static String extractFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

}
