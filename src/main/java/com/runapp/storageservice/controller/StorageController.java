package com.runapp.storageservice.controller;


import com.runapp.storageservice.dto.request.DeleteRequest;
import com.runapp.storageservice.dto.response.DeleteResponse;
import com.runapp.storageservice.dto.response.StorageResponse;
import com.runapp.storageservice.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@RequestMapping("/storage")
@Tag(name = "Storage Management", description = "Operations related to storage")
public class StorageController {

    StorageService storageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Upload a file to the storage")
    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<StorageResponse> uploadFile(@Parameter(description = "file", required = true) @RequestPart("file") MultipartFile file,
                                                      @Parameter(description = "directory", required = true) @RequestPart("directory") String directory) throws IOException {
        return storageService.uploadFile(file, directory);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a file", description = "Delete a file from the storage")
    @ApiResponse(responseCode = "200", description = "File deleted successfully", content = @Content(schema = @Schema(implementation = DeleteRequest.class)))
    @ApiResponse(responseCode = "404", description = "File not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<DeleteResponse> deleteFile(@RequestBody DeleteRequest deleteRequest) throws URISyntaxException, IOException {
        return storageService.deleteFile(deleteRequest);
    }
}
