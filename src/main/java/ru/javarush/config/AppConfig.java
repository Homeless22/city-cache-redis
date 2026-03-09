package ru.javarush.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final Properties properties;

    public AppConfig(String configFile) {
        properties = loadProperties(configFile);
    }

    public String getPropertyValue(String propertyName) {
        if (!properties.containsKey(propertyName)) {
            return null;
        }
        return properties.getProperty(propertyName);
    }

    private Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new FileNotFoundException("Файл не найден: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла " + fileName + ": " + e.getMessage(), e);
        }
        return props;
    }
}
