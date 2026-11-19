package vn.edu.englishcenter.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=QuanLyTrungTamTiengAnh;"
            + "encrypt=false;"
            + "trustServerCertificate=true;";
    private static final String USER = "trangdai";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Khong tim thay driver SQL Server JDBC (mssql-jdbc). Hay them file .jar vao Libraries!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
