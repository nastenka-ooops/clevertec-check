package ru.clevertec.check.config;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseConfig() {
        this.url = System.getProperty("datasource.url");
        this.username = System.getProperty("datasource.username");
        this.password = System.getProperty("datasource.password");
    }

    public Connection getConnection() throws SQLException {
        Properties info = new Properties();
        if (username != null) {
            info.put("user", username);
        }
        if (password != null) {
            info.put("password", password);
        }
        return new Driver().connect(url, info);
    }
}
