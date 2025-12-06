package com.factory.ml.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and manages application configuration from properties files.
 * 
 * This utility class reads configuration properties from a file and provides
 * type-safe access methods for different property types including strings,
 * integers, and booleans.
 * 
 * <p>Project Context:
 * Used throughout the factory-ml-offline-system Java application to load
 * configuration settings for model paths, thresholds, and application behavior.
 * 
 * <p>Usage Example:
 * <pre>
 * ConfigLoader config = new ConfigLoader("config/app.properties");
 * String modelPath = config.getProperty("model.path");
 * int batchSize = config.getIntProperty("batch.size");
 * boolean enableLogging = config.getBooleanProperty("logging.enabled");
 * </pre>
 */
public class ConfigLoader {
    private Properties properties;

    /**
     * Constructs a ConfigLoader and loads properties from the specified file.
     * 
     * @param configFilePath Path to the properties configuration file
     */
    public ConfigLoader(String configFilePath) {
        properties = new Properties();
        loadConfig(configFilePath);
    }

    /**
     * Loads properties from the configuration file.
     * 
     * Reads the properties file and populates the internal Properties object.
     * Prints stack trace if an IOException occurs during loading.
     * 
     * @param configFilePath Path to the properties configuration file
     */
    private void loadConfig(String configFilePath) {
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves a string property value.
     * 
     * @param key Property key to retrieve
     * @return Property value as string, or null if key not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves an integer property value.
     * 
     * @param key Property key to retrieve
     * @return Property value parsed as integer
     * @throws NumberFormatException if the property value is not a valid integer
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * Retrieves a boolean property value.
     * 
     * @param key Property key to retrieve
     * @return Property value parsed as boolean
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}