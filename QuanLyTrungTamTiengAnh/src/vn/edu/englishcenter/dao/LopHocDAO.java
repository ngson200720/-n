package vn.edu.englishcenter.dao;

import vn.edu.englishcenter.config.DatabaseConnection;
import vn.edu.englishcenter.model.ComboItem;
import vn.edu.englishcenter.model.LopHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopHocDAO {

    public List<LopHoc> getAll() throws SQLException {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT MaLop, TenLop, MaKhoaHoc, MaGV, MaPhong, MaCa, "
                   + "NgayKhaiGiang, NgayKetThucDuKien, SiSoToiDa, SiSoHienTai, TrangThaiLop "
                   + "FROM LopHoc ORDER BY MaLop";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<LopHoc> search(String keyword) throws SQLException {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT MaLop, TenLop, MaKhoaHoc, MaGV, MaPhong, MaCa, "
                   + "NgayKhaiGiang, NgayKetThucDuKien, SiSoToiDa, SiSoHienTai, TrangThaiLop "
                   + "FROM LopHoc "
                   + "WHERE MaLop LIKE ? OR TenLop LIKE ? OR MaKhoaHoc LIKE ? OR MaGV LIKE ? OR TrangThaiLop LIKE ? "
                   + "ORDER BY MaLop";
        String pattern = "%" + keyword.trim() + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setNString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setNString(5, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean insert(LopHoc lh) throws SQLException {
        String sql = "INSERT INTO LopHoc (MaLop, TenLop, MaKhoaHoc, MaGV, MaPhong, MaCa, "
                   + "NgayKhaiGiang, NgayKetThucDuKien, SiSoToiDa, TrangThaiLop) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lh.getMaLop());
            ps.setNString(2, lh.getTenLop());
            ps.setString(3, lh.getMaKhoaHoc());
            ps.setString(4, lh.getMaGV());
            ps.setString(5, lh.getMaPhong());
            ps.setString(6, lh.getMaCa());
            ps.setDate(7, lh.getNgayKhaiGiang());
            ps.setDate(8, lh.getNgayKetThucDuKien());
            ps.setInt(9, lh.getSiSoToiDa());
            ps.setNString(10, lh.getTrangThaiLop());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(LopHoc lh) throws SQLException {
        String sql = "UPDATE LopHoc SET TenLop = ?, MaKhoaHoc = ?, MaGV = ?, MaPhong = ?, MaCa = ?, "
                   + "NgayKhaiGiang = ?, NgayKetThucDuKien = ?, SiSoToiDa = ?, TrangThaiLop = ? "
                   + "WHERE MaLop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, lh.getTenLop());
            ps.setString(2, lh.getMaKhoaHoc());
            ps.setString(3, lh.getMaGV());
            ps.setString(4, lh.getMaPhong());
            ps.setString(5, lh.getMaCa());
            ps.setDate(6, lh.getNgayKhaiGiang());
            ps.setDate(7, lh.getNgayKetThucDuKien());
            ps.setInt(8, lh.getSiSoToiDa());
            ps.setNString(9, lh.getTrangThaiLop());
            ps.setString(10, lh.getMaLop());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maLop) throws SQLException {
        String sql = "DELETE FROM LopHoc WHERE MaLop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        }
    }

    public List<ComboItem> getComboData(String sql) throws SQLException {
        List<ComboItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ComboItem(rs.getString(1), rs.getNString(2)));
            }
        }
        return list;
    }

    public List<ComboItem> getKhoaHocCombo() throws SQLException {
        return getComboData("SELECT MaKhoaHoc, TenKhoaHoc FROM KhoaHoc ORDER BY MaKhoaHoc");
    }

    public List<ComboItem> getGiangVienCombo() throws SQLException {
        return getComboData("SELECT MaGV, HoTen FROM GiangVien ORDER BY MaGV");
    }

    public List<ComboItem> getPhongHocCombo() throws SQLException {
        return getComboData("SELECT MaPhong, TenPhong FROM PhongHoc ORDER BY MaPhong");
    }

    public List<ComboItem> getCaHocCombo() throws SQLException {
        return getComboData("SELECT MaCa, TenCa FROM CaHoc ORDER BY MaCa");
    }

    private LopHoc mapRow(ResultSet rs) throws SQLException {
        return new LopHoc(
                rs.getString("MaLop"),
                rs.getNString("TenLop"),
                rs.getString("MaKhoaHoc"),
                rs.getString("MaGV"),
                rs.getString("MaPhong"),
                rs.getString("MaCa"),
                rs.getDate("NgayKhaiGiang"),
                rs.getDate("NgayKetThucDuKien"),
                rs.getInt("SiSoToiDa"),
                rs.getInt("SiSoHienTai"),
                rs.getNString("TrangThaiLop")
        );
    }
}
