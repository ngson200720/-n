package vn.edu.englishcenter.dao;

import vn.edu.englishcenter.config.DatabaseConnection;
import vn.edu.englishcenter.model.ComboItem;
import vn.edu.englishcenter.model.KhoaHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocDAO {

    public List<KhoaHoc> getAll() throws SQLException {
        List<KhoaHoc> list = new ArrayList<>();
        String sql = "SELECT MaKhoaHoc, TenKhoaHoc, MaCapDo, TongSoBuoi, ThoiLuongMoiBuoi, "
                   + "HocPhiNiemYet, GiaoTrinh, TrangThaiMo FROM KhoaHoc ORDER BY MaKhoaHoc";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<KhoaHoc> search(String keyword) throws SQLException {
        List<KhoaHoc> list = new ArrayList<>();
        String sql = "SELECT MaKhoaHoc, TenKhoaHoc, MaCapDo, TongSoBuoi, ThoiLuongMoiBuoi, "
                   + "HocPhiNiemYet, GiaoTrinh, TrangThaiMo FROM KhoaHoc "
                   + "WHERE MaKhoaHoc LIKE ? OR TenKhoaHoc LIKE ? OR MaCapDo LIKE ? OR GiaoTrinh LIKE ? "
                   + "ORDER BY MaKhoaHoc";
        String pattern = "%" + keyword.trim() + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setNString(2, pattern);
            ps.setString(3, pattern);
            ps.setNString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<ComboItem> getAllCapDo() throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        String sql = "SELECT MaCapDo, TenCapDo FROM CapDo ORDER BY MaCapDo";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ComboItem(rs.getString("MaCapDo"), rs.getNString("TenCapDo")));
            }
        }
        return list;
    }

    public boolean insert(KhoaHoc kh) throws SQLException {
        String sql = "INSERT INTO KhoaHoc (MaKhoaHoc, TenKhoaHoc, MaCapDo, TongSoBuoi, "
                   + "ThoiLuongMoiBuoi, HocPhiNiemYet, GiaoTrinh, TrangThaiMo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhoaHoc());
            ps.setNString(2, kh.getTenKhoaHoc());
            ps.setString(3, kh.getMaCapDo());
            ps.setInt(4, kh.getTongSoBuoi());
            ps.setInt(5, kh.getThoiLuongMoiBuoi());
            ps.setBigDecimal(6, kh.getHocPhiNiemYet());
            ps.setNString(7, kh.getGiaoTrinh());
            ps.setBoolean(8, kh.isTrangThaiMo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(KhoaHoc kh) throws SQLException {
        String sql = "UPDATE KhoaHoc SET TenKhoaHoc = ?, MaCapDo = ?, TongSoBuoi = ?, "
                   + "ThoiLuongMoiBuoi = ?, HocPhiNiemYet = ?, GiaoTrinh = ?, TrangThaiMo = ? "
                   + "WHERE MaKhoaHoc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, kh.getTenKhoaHoc());
            ps.setString(2, kh.getMaCapDo());
            ps.setInt(3, kh.getTongSoBuoi());
            ps.setInt(4, kh.getThoiLuongMoiBuoi());
            ps.setBigDecimal(5, kh.getHocPhiNiemYet());
            ps.setNString(6, kh.getGiaoTrinh());
            ps.setBoolean(7, kh.isTrangThaiMo());
            ps.setString(8, kh.getMaKhoaHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maKhoaHoc) throws SQLException {
        String sql = "DELETE FROM KhoaHoc WHERE MaKhoaHoc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoaHoc);
            return ps.executeUpdate() > 0;
        }
    }

    private KhoaHoc mapRow(ResultSet rs) throws SQLException {
        return new KhoaHoc(
                rs.getString("MaKhoaHoc"),
                rs.getNString("TenKhoaHoc"),
                rs.getString("MaCapDo"),
                rs.getInt("TongSoBuoi"),
                rs.getInt("ThoiLuongMoiBuoi"),
                rs.getBigDecimal("HocPhiNiemYet"),
                rs.getNString("GiaoTrinh"),
                rs.getBoolean("TrangThaiMo")
        );
    }
}
