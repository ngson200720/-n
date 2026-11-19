package vn.edu.englishcenter.model;

import java.math.BigDecimal;

public class KhoaHoc {
    private String maKhoaHoc;
    private String tenKhoaHoc;
    private String maCapDo;
    private int tongSoBuoi;
    private int thoiLuongMoiBuoi;
    private BigDecimal hocPhiNiemYet;
    private String giaoTrinh;
    private boolean trangThaiMo;

    public KhoaHoc() {}

    public KhoaHoc(String maKhoaHoc, String tenKhoaHoc, String maCapDo,
                   int tongSoBuoi, int thoiLuongMoiBuoi, BigDecimal hocPhiNiemYet,
                   String giaoTrinh, boolean trangThaiMo) {
        this.maKhoaHoc = maKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
        this.maCapDo = maCapDo;
        this.tongSoBuoi = tongSoBuoi;
        this.thoiLuongMoiBuoi = thoiLuongMoiBuoi;
        this.hocPhiNiemYet = hocPhiNiemYet;
        this.giaoTrinh = giaoTrinh;
        this.trangThaiMo = trangThaiMo;
    }

    public String getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(String maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }

    public String getMaCapDo() { return maCapDo; }
    public void setMaCapDo(String maCapDo) { this.maCapDo = maCapDo; }

    public int getTongSoBuoi() { return tongSoBuoi; }
    public void setTongSoBuoi(int tongSoBuoi) { this.tongSoBuoi = tongSoBuoi; }

    public int getThoiLuongMoiBuoi() { return thoiLuongMoiBuoi; }
    public void setThoiLuongMoiBuoi(int thoiLuongMoiBuoi) { this.thoiLuongMoiBuoi = thoiLuongMoiBuoi; }

    public BigDecimal getHocPhiNiemYet() { return hocPhiNiemYet; }
    public void setHocPhiNiemYet(BigDecimal hocPhiNiemYet) { this.hocPhiNiemYet = hocPhiNiemYet; }

    public String getGiaoTrinh() { return giaoTrinh; }
    public void setGiaoTrinh(String giaoTrinh) { this.giaoTrinh = giaoTrinh; }

    public boolean isTrangThaiMo() { return trangThaiMo; }
    public void setTrangThaiMo(boolean trangThaiMo) { this.trangThaiMo = trangThaiMo; }
}
