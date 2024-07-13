package org.example;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class AppConfig {

    private final Properties properties = new Properties();
    private String sourceFilePath;
    private String outputFileWhole;
    private String outputFileBatch;
    private int batchSize;

    AppConfig() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (inputStream != null) {
                properties.load(inputStream);
                loadProperties();
            } else {
                throw new RuntimeException("Failed to load config.properties file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        sourceFilePath = properties.getProperty("sourceFile");
        outputFileWhole = properties.getProperty("outputFileWhole");
        outputFileBatch = properties.getProperty("outputFileBatch");

        try {
            batchSize = Integer.parseInt(properties.getProperty("batchSize"));
        } catch (NumberFormatException e) {
            batchSize = 100;
        }
    }
}