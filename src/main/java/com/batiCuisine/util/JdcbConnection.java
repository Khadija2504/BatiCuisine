package com.batiCuisine.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdcbConnection {
    private static final String URL = "jdbc:postgresql://localhost:5433/BatiCuisine";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
