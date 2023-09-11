package com.runapp.storageservice.controller;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RestController
@RequestMapping("/storage")
public class StorageController {

    @Value("${spring.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        ;
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try (InputStream inputStream = file.getInputStream()) {
            storage.create(blobInfo, inputStream);
        }
        String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }
}
