package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized JDBC connection helper for the osmar_db database.
 *
 * Every *DAO class should call {@link #getConnection()} rather than
 * opening its own connection, so connection settings live in exactly
 * one place. Update URL / USER / PASSWORD to match your local MySQL
 * setup.
 */
public final class DB_connection {

    private static final String URL = "jdbc:mysql://localhost:3306/osmar_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DB_connection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
