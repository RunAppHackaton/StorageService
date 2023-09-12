package com.runapp.storageservice.dto.response;

public class DeleteResponse {
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public DeleteResponse(String response) {
        this.response = response;
    }

    public DeleteResponse() {
    }
}
