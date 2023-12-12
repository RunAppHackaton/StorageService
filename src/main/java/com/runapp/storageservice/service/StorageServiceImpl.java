package com.runapp.storageservice.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.runapp.storageservice.dto.request.DeleteRequest;
import com.runapp.storageservice.dto.response.DeleteResponse;
import com.runapp.storageservice.dto.response.StorageResponse;
import com.runapp.storageservice.exception.IoException;
import com.runapp.storageservice.exception.NoEntityFoundException;
import com.runapp.storageservice.exception.UncorrectedUriException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    @Value("${spring.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;

    @Override
    public ResponseEntity<StorageResponse> uploadFile(MultipartFile file, String directory) {
        try {
            String fileName = directory + UUID.randomUUID();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            InputStream inputStream = file.getInputStream();
            storage.create(blobInfo, inputStream);
            String fileUrl = "https://storage.cloud.google.com/" + bucketName + "/" + fileName;
            return ResponseEntity.ok(new StorageResponse(fileUrl));
        } catch (IOException e) {
            throw new IoException("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<DeleteResponse> deleteFile(DeleteRequest deleteRequest) {
        try {
            URI uri = new URI(deleteRequest.getFile_uri());
            String path = uri.getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            fileName = deleteRequest.getDirectory() + fileName;

            Blob blob = storage.get(bucketName, fileName);
            Storage.BlobSourceOption precondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            if (storage.delete(bucketName, fileName, precondition)) {
                return ResponseEntity.ok(new DeleteResponse("File deleted successfully"));
            } else {
                throw new NoEntityFoundException("File not found or unable to delete");
            }
        } catch (URISyntaxException e) {
            throw new UncorrectedUriException("Error deleting file: " + e.getMessage());
        }
    }
}
