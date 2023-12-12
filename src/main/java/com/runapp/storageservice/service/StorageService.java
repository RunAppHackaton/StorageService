package com.runapp.storageservice.service;

import com.runapp.storageservice.dto.request.DeleteRequest;
import com.runapp.storageservice.dto.response.DeleteResponse;
import com.runapp.storageservice.dto.response.StorageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface StorageService {

    ResponseEntity<StorageResponse> uploadFile(MultipartFile file, String directory) throws IOException;

    ResponseEntity<DeleteResponse> deleteFile(DeleteRequest deleteRequest) throws URISyntaxException, IOException;
}
