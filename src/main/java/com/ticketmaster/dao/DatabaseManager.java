package com.ticketmaster.dao;

import com.ticketmaster.config.ConfigManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private String databasePath;

    private DatabaseManager() {
        loadDriver();
        databasePath = ConfigManager.getInstance().getDatabasePath();
        initializeDatabase();
    }

    private void loadDriver() {
        try {
            // Try to load the driver class
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            // Try using ServiceLoader (Java 6+)
            try {
                java.util.ServiceLoader<java.sql.Driver> drivers = 
                    java.util.ServiceLoader.load(java.sql.Driver.class);
                for (java.sql.Driver driver : drivers) {
                    if (driver.getClass().getName().equals("org.sqlite.JDBC")) {
                        java.sql.DriverManager.registerDriver(driver);
                        System.out.println("SQLite JDBC driver loaded via ServiceLoader");
                        return;
                    }
                }
            } catch (Exception ex) {
                // Ignore
            }
            System.err.println("Warning: Could not load SQLite JDBC driver explicitly.");
            System.err.println("ClassNotFoundException: " + e.getMessage());
            System.err.println("Please ensure sqlite-jdbc dependency is in classpath.");
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(0);
    }
    
    private Connection getConnection(int retryCount) throws SQLException {
        File dbFile = new File(databasePath);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            // Enable WAL mode in connection URL for better concurrency
            String url = "jdbc:sqlite:" + databasePath;
            Connection conn = DriverManager.getConnection(url);
            
            // Set pragmas after connection (WAL mode should be set once, but we set it for safety)
            try (var stmt = conn.createStatement()) {
                // Only set WAL if not already set (to avoid errors)
                stmt.execute("PRAGMA busy_timeout=10000"); // 10 second timeout
                stmt.execute("PRAGMA foreign_keys=ON");
                // Try to set WAL mode, but don't fail if it's already set
                try {
                    stmt.execute("PRAGMA journal_mode=WAL");
                } catch (SQLException walEx) {
                    // WAL might already be set, ignore
                }
            }
            
            return conn;
        } catch (SQLException e) {
            if (e.getMessage().contains("No suitable driver")) {
                throw new SQLException(
                    "SQLite JDBC driver not found. Please ensure Maven dependencies are loaded.\n" +
                    "In IntelliJ: File > Reload All Maven Projects\n" +
                    "In Eclipse: Right-click project > Maven > Update Project\n" +
                    "Or run: mvn clean install", e);
            }
            // If database is locked, wait a bit and retry (max 3 times)
            if ((e.getMessage().contains("locked") || e.getMessage().contains("SQLITE_BUSY")) && retryCount < 3) {
                try {
                    Thread.sleep(200 * (retryCount + 1)); // Exponential backoff
                    return getConnection(retryCount + 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Connection interrupted", ie);
                }
            }
            throw e;
        }
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Users table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password_hash TEXT NOT NULL, " +
                "profile TEXT NOT NULL, " +
                "password_changed INTEGER NOT NULL DEFAULT 0)"
            );

            // Tickets table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "criticality_level TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "created_by TEXT NOT NULL, " +
                "created_at TEXT NOT NULL, " +
                "result_description TEXT)"
            );
            
            // Add result_description column if it doesn't exist (for existing databases)
            try {
                conn.createStatement().execute("ALTER TABLE tickets ADD COLUMN result_description TEXT");
            } catch (SQLException e) {
                // Column already exists, ignore
            }

            // Ticket images table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS ticket_images (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ticket_id INTEGER NOT NULL, " +
                "image_path TEXT NOT NULL, " +
                "FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)"
            );

            // Ticket updates/history table
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS ticket_updates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ticket_id INTEGER NOT NULL, " +
                "updated_at TEXT NOT NULL, " +
                "updated_by TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "description TEXT, " +
                "FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)"
            );

            // Insert default user if not exists
            conn.createStatement().execute(
                "INSERT OR IGNORE INTO users (username, password_hash, profile, password_changed) " +
                "VALUES ('admin', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'BANKA', 0)"
            );
            // test123 SHA-256 hash
            String test123Hash = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
            conn.createStatement().execute(
                "INSERT OR IGNORE INTO users (username, password_hash, profile, password_changed) " +
                "VALUES ('test', '" + test123Hash + "', 'FIRMA', 0)"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

