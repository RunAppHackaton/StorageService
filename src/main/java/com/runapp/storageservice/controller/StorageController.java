package com.runapp.storageservice.controller;

import com.google.cloud.storage.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


@RestController
@RequestMapping("/storage")
@Tag(name = "Storage Management", description = "Operations related to storage")
public class StorageController {

    @Value("${spring.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;


    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Upload a file to the storage")
    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<Object> uploadFile(@Parameter(description = "file", required = true) @RequestParam("file") MultipartFile file,
                                             @Parameter(description = "directory", required = true) @RequestParam("directory") String directory) throws IOException {
        String fileName = directory + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try (InputStream inputStream = file.getInputStream()) {
            storage.create(blobInfo, inputStream);
        }
        String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a file", description = "Delete a file from the storage")
    @ApiResponse(responseCode = "200", description = "File deleted successfully")
    @ApiResponse(responseCode = "404", description = "File not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Object> deleteFile(@Parameter(description = "file_uri", required = true) @RequestParam("file_uri") String fileUri,
                                             @Parameter(description = "directory", required = true) @RequestParam("directory") String directory) throws URISyntaxException {
        try {
            // Извлекаем имя файла из URI
            URI uri = new URI(fileUri);
            String path = uri.getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            fileName = directory + fileName;

            System.out.println(fileName);

            // Создаем BlobId для указания бакета и имени объекта Blob
            Blob blob = storage.get(bucketName, fileName);
            Storage.BlobSourceOption precondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            if (blob != null && storage.delete(bucketName, fileName, precondition)) {
                // Объект Blob успешно удален
                return ResponseEntity.ok("File deleted successfully");
            } else {
                // Объект Blob не найден или не удалось его удалить
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or unable to delete");
            }
        } catch (Exception e) {
            // Ошибка при удалении
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the file");
        }
    }
}
