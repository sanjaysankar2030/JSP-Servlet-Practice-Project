package com.myapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * ------------------
 * This class is responsible for creating a connection to the MySQL database.
 * It uses the JDBC DriverManager to open a connection using your DB credentials.
 *
 * HOW IT WORKS:
 *   - We define the DB URL, username, and password as constants.
 *   - The getConnection() method returns a live Connection object.
 *   - Always close the connection after use (in a finally block or try-with-resources).
 */
public class DBConnection {

    // ✅ STEP 1: Change these values to match your MySQL setup
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/myapp_db";
    private static final String DB_USER     = "root";        // your MySQL username
    private static final String DB_PASSWORD = "yourpassword"; // your MySQL password

    /**
     * Returns a new database connection.
     * Call this method whenever you need to run a SQL query.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver class (required for some environments)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add it to pom.xml.", e);
        }

        // Open and return the connection
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
