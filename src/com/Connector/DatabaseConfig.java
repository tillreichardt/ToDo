package com.Connector;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    public static Properties loadProperties() {
        Properties props = new Properties();
        String path = "resources/config.properties"; // Relativer Pfad
        try (FileInputStream input = new FileInputStream(path)) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }
}
