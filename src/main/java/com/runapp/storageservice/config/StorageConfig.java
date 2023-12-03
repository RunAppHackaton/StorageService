package com.runapp.storageservice.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {
        // Загрузка ключа сервисного аккаунта из файла JSON
//        FileInputStream credentialsStream = new FileInputStream("/app/credentials.json");
        FileInputStream credentialsStream = new FileInputStream("/Users/denpool/Desktop/Credentials/credentials.json");
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);

        // Использование ключа сервисного аккаунта для создания объекта Storage
        StorageOptions options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build();

        return options.getService();
    }
}

