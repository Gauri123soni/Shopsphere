package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = setting(
            "shopsphere.db.url",
            "SHOPSPHERE_DB_URL",
            "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC");
    private static final String USERNAME = setting(
            "shopsphere.db.user",
            "SHOPSPHERE_DB_USER",
            "root");
    private static final String PASSWORD = setting(
            "shopsphere.db.password",
            "SHOPSPHERE_DB_PASSWORD",
            "");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("The MySQL JDBC driver is not available.", e);
        }
    }

    private static String setting(String property, String environmentVariable, String defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(environmentVariable);
        }
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
