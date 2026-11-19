package vn.edu.englishcenter.dao;

import vn.edu.englishcenter.config.DatabaseConnection;
import vn.edu.englishcenter.model.HocVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocVienDAO {

    public List<HocVien> getAll() throws SQLException {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT MaHV, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, DiaChi, "
                   + "HoTenPhuHuynh, DiemTestDauVao, TrangThai FROM HocVien ORDER BY MaHV";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<HocVien> search(String keyword) throws SQLException {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT MaHV, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, DiaChi, "
                   + "HoTenPhuHuynh, DiemTestDauVao, TrangThai FROM HocVien "
                   + "WHERE MaHV LIKE ? OR HoTen LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ? "
                   + "ORDER BY MaHV";
        String pattern = "%" + keyword.trim() + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setNString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(HocVien hv) throws SQLException {
        String sql = "INSERT INTO HocVien (MaHV, HoTen, NgaySinh, GioiTinh, SoDienThoai, Email, "
                   + "DiaChi, HoTenPhuHuynh, DiemTestDauVao, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hv.getMaHV());
            ps.setNString(2, hv.getHoTen());
            ps.setDate(3, hv.getNgaySinh());
            ps.setNString(4, hv.getGioiTinh());
            ps.setString(5, hv.getSoDienThoai());
            setNullableString(ps, 6, hv.getEmail());
            setNullableNString(ps, 7, hv.getDiaChi());
            setNullableNString(ps, 8, hv.getHoTenPhuHuynh());
            ps.setBigDecimal(9, hv.getDiemTestDauVao());
            ps.setNString(10, hv.getTrangThai());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(HocVien hv) throws SQLException {
        String sql = "UPDATE HocVien SET HoTen = ?, NgaySinh = ?, GioiTinh = ?, SoDienThoai = ?, "
                   + "Email = ?, DiaChi = ?, HoTenPhuHuynh = ?, DiemTestDauVao = ?, TrangThai = ? "
                   + "WHERE MaHV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, hv.getHoTen());
            ps.setDate(2, hv.getNgaySinh());
            ps.setNString(3, hv.getGioiTinh());
            ps.setString(4, hv.getSoDienThoai());
            setNullableString(ps, 5, hv.getEmail());
            setNullableNString(ps, 6, hv.getDiaChi());
            setNullableNString(ps, 7, hv.getHoTenPhuHuynh());
            ps.setBigDecimal(8, hv.getDiemTestDauVao());
            ps.setNString(9, hv.getTrangThai());
            ps.setString(10, hv.getMaHV());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maHV) throws SQLException {
        String sql = "DELETE FROM HocVien WHERE MaHV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHV);
            return ps.executeUpdate() > 0;
        }
    }

    private HocVien mapRow(ResultSet rs) throws SQLException {
        return new HocVien(
                rs.getString("MaHV"),
                rs.getNString("HoTen"),
                rs.getDate("NgaySinh"),
                rs.getNString("GioiTinh"),
                rs.getString("SoDienThoai"),
                rs.getString("Email"),
                rs.getNString("DiaChi"),
                rs.getNString("HoTenPhuHuynh"),
                rs.getBigDecimal("DiemTestDauVao"),
                rs.getNString("TrangThai")
        );
    }

    private void setNullableString(PreparedStatement ps, int idx, String val) throws SQLException {
        if (val == null || val.trim().isEmpty()) {
            ps.setNull(idx, Types.VARCHAR);
        } else {
            ps.setString(idx, val.trim());
        }
    }

    private void setNullableNString(PreparedStatement ps, int idx, String val) throws SQLException {
        if (val == null || val.trim().isEmpty()) {
            ps.setNull(idx, Types.NVARCHAR);
        } else {
            ps.setNString(idx, val.trim());
        }
    }
}
