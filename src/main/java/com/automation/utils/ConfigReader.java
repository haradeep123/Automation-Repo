package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader utility to read properties from config files
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    
    /**
     * Load properties from config file
     */
    public static void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config properties: " + e.getMessage());
        }
    }
    
    /**
     * Get property value by key
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default value
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get browser from config
     */
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Get URL from config
     */
    public static String getUrl() {
        return getProperty("url");
    }
    
    /**
     * Get implicit wait timeout
     */
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }
    
    /**
     * Get explicit wait timeout
     */
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "10"));
    }
}
