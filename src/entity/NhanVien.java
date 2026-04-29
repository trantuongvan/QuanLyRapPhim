package entity;

import java.time.LocalDate;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String diaChi;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String email;
    private String gioiTinh;

    public NhanVien() {
        this("", "", "", "", LocalDate.now(), "", "");
    }

    public NhanVien(String maNV, String tenNV, String diaChi, String soDienThoai,
                    LocalDate ngaySinh, String email, String gioiTinh) {
        setMaNV(maNV);
        setTenNV(tenNV);
        setDiaChi(diaChi);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setEmail(email);
        setGioiTinh(gioiTinh);
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty())
            throw new IllegalArgumentException("Mã nhân viên không được rỗng!");
        this.maNV = maNV.trim();
    }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) {
        if (tenNV == null || tenNV.trim().isEmpty())
            throw new IllegalArgumentException("Tên nhân viên không được rỗng!");
        this.tenNV = tenNV.trim();
    }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi == null ? "" : diaChi.trim();
    }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai == null ? "" : soDienThoai.trim();
    }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh != null ? ngaySinh : LocalDate.now();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh == null ? "" : gioiTinh.trim();
    }

    @Override
    public String toString() {
        return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV +
               ", diaChi=" + diaChi + ", soDienThoai=" + soDienThoai +
               ", ngaySinh=" + ngaySinh + ", email=" + email +
               ", gioiTinh=" + gioiTinh + "]";
    }
}
