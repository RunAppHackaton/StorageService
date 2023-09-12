package com.runapp.storageservice.controller;

import com.google.cloud.storage.*;
import com.runapp.storageservice.dto.request.DeleteRequest;
import com.runapp.storageservice.dto.response.DeleteResponse;
import com.runapp.storageservice.dto.response.StorageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public ResponseEntity<Object> uploadFile(@Parameter(description = "file", required = true) @RequestPart("file") MultipartFile file,
                                             @Parameter(description = "directory", required = true) @RequestPart("directory") String directory) throws IOException {
        String fileName = directory + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try (InputStream inputStream = file.getInputStream()) {
            storage.create(blobInfo, inputStream);
        }
        String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        return ResponseEntity.ok(new StorageResponse(fileUrl));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a file", description = "Delete a file from the storage")
    @ApiResponse(responseCode = "200", description = "File deleted successfully", content = @Content(schema = @Schema(implementation = DeleteRequest.class)))
    @ApiResponse(responseCode = "404", description = "File not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Object> deleteFile(@RequestBody DeleteRequest deleteRequest) throws URISyntaxException {
        try {
            URI uri = new URI(deleteRequest.getFile_uri());
            String path = uri.getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            fileName = deleteRequest.getDirectory() + fileName;

            Blob blob = storage.get(bucketName, fileName);
            Storage.BlobSourceOption precondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            if (blob != null && storage.delete(bucketName, fileName, precondition)) {
                return ResponseEntity.ok(new DeleteResponse("File deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DeleteResponse("File not found or unable to delete"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse("An error occurred while deleting the file"));
        }
    }
}
