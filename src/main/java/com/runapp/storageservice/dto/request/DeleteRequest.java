package com.runapp.storageservice.dto.request;

public class DeleteRequest {

    private String file_uri;
    private String directory;

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public DeleteRequest(String file_uri, String directory) {
        this.file_uri = file_uri;
        this.directory = directory;
    }
}
