package vn.edu.englishcenter.model;

public class TaiKhoan {
    private int maTaiKhoan;
    private String tenDangNhap;
    private String matKhauHash;
    private String maVaiTro;
    private boolean trangThai;

    public TaiKhoan() {}

    public TaiKhoan(int maTaiKhoan, String tenDangNhap, String matKhauHash, String maVaiTro, boolean trangThai) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhauHash = matKhauHash;
        this.maVaiTro = maVaiTro;
        this.trangThai = trangThai;
    }

    public int getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(int maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhauHash() { return matKhauHash; }
    public void setMatKhauHash(String matKhauHash) { this.matKhauHash = matKhauHash; }

    public String getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(String maVaiTro) { this.maVaiTro = maVaiTro; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
