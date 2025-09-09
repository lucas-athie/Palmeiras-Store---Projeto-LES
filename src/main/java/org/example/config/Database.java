package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL      = "jdbc:postgresql://localhost:5432/PalmeirasStore";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    private Database() {  }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static synchronized void closeConnection(Connection conn) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}