package com.ticketmaster.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        // Try to load from root directory first
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            // Try to load from resources
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (is != null) {
                    properties.load(is);
                } else {
                    // If config file doesn't exist, create default
                    properties.setProperty("database.path", "./data/ticketmaster.db");
                    saveConfig();
                }
            } catch (IOException ex) {
                // If config file doesn't exist, create default
                properties.setProperty("database.path", "./data/ticketmaster.db");
                saveConfig();
            }
        }
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "TicketMaster Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDatabasePath() {
        return properties.getProperty("database.path", "./data/ticketmaster.db");
    }

    public void setDatabasePath(String path) {
        properties.setProperty("database.path", path);
        saveConfig();
    }
}

