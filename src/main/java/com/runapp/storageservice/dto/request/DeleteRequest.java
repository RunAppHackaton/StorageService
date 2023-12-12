package com.runapp.storageservice.dto.request;

import lombok.Data;

@Data
public class DeleteRequest {

    private String file_uri;
    private String directory;

}
