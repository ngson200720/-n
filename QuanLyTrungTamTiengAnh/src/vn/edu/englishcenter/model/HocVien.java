package vn.edu.englishcenter.model;

import java.math.BigDecimal;
import java.sql.Date;

public class HocVien {
    private String maHV;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String hoTenPhuHuynh;
    private BigDecimal diemTestDauVao;
    private String trangThai;

    public HocVien() {}

    public HocVien(String maHV, String hoTen, Date ngaySinh, String gioiTinh,
                   String soDienThoai, String email, String diaChi,
                   String hoTenPhuHuynh, BigDecimal diemTestDauVao, String trangThai) {
        this.maHV = maHV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.hoTenPhuHuynh = hoTenPhuHuynh;
        this.diemTestDauVao = diemTestDauVao;
        this.trangThai = trangThai;
    }

    public String getMaHV() { return maHV; }
    public void setMaHV(String maHV) { this.maHV = maHV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getHoTenPhuHuynh() { return hoTenPhuHuynh; }
    public void setHoTenPhuHuynh(String hoTenPhuHuynh) { this.hoTenPhuHuynh = hoTenPhuHuynh; }

    public BigDecimal getDiemTestDauVao() { return diemTestDauVao; }
    public void setDiemTestDauVao(BigDecimal diemTestDauVao) { this.diemTestDauVao = diemTestDauVao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
