package vn.edu.englishcenter.model;

import java.sql.Date;

public class LopHoc {
    private String maLop;
    private String tenLop;
    private String maKhoaHoc;
    private String maGV;
    private String maPhong;
    private String maCa;
    private Date ngayKhaiGiang;
    private Date ngayKetThucDuKien;
    private int siSoToiDa;
    private int siSoHienTai;
    private String trangThaiLop;

    public LopHoc() {}

    public LopHoc(String maLop, String tenLop, String maKhoaHoc, String maGV,
                  String maPhong, String maCa, Date ngayKhaiGiang,
                  Date ngayKetThucDuKien, int siSoToiDa, int siSoHienTai, String trangThaiLop) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoaHoc = maKhoaHoc;
        this.maGV = maGV;
        this.maPhong = maPhong;
        this.maCa = maCa;
        this.ngayKhaiGiang = ngayKhaiGiang;
        this.ngayKetThucDuKien = ngayKetThucDuKien;
        this.siSoToiDa = siSoToiDa;
        this.siSoHienTai = siSoHienTai;
        this.trangThaiLop = trangThaiLop;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(String maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getMaCa() { return maCa; }
    public void setMaCa(String maCa) { this.maCa = maCa; }

    public Date getNgayKhaiGiang() { return ngayKhaiGiang; }
    public void setNgayKhaiGiang(Date ngayKhaiGiang) { this.ngayKhaiGiang = ngayKhaiGiang; }

    public Date getNgayKetThucDuKien() { return ngayKetThucDuKien; }
    public void setNgayKetThucDuKien(Date ngayKetThucDuKien) { this.ngayKetThucDuKien = ngayKetThucDuKien; }

    public int getSiSoToiDa() { return siSoToiDa; }
    public void setSiSoToiDa(int siSoToiDa) { this.siSoToiDa = siSoToiDa; }

    public int getSiSoHienTai() { return siSoHienTai; }
    public void setSiSoHienTai(int siSoHienTai) { this.siSoHienTai = siSoHienTai; }

    public String getTrangThaiLop() { return trangThaiLop; }
    public void setTrangThaiLop(String trangThaiLop) { this.trangThaiLop = trangThaiLop; }
}
