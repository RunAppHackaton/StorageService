package com.runapp.storageservice.dto.response;
public class StorageResponse {

    private String file_uri;

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }

    public StorageResponse(String file_uri) {
        this.file_uri = file_uri;
    }
}
