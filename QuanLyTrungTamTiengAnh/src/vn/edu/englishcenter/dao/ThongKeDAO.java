package vn.edu.englishcenter.dao;

import vn.edu.englishcenter.config.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThongKeDAO {
    public int demTongHocVien() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM HocVien");
    }

    public int demTongKhoaHoc() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM KhoaHoc");
    }

    public int demTongLopHoc() throws SQLException {
        return executeCount("SELECT COUNT(*) FROM LopHoc");
    }

    public BigDecimal tinhTongDoanhThu() throws SQLException {
        String sql = "SELECT ISNULL(SUM(SoTienThu), 0) FROM HoaDon";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    private int executeCount(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
