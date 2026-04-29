package entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String gioiTinh;
    private String soDT;
    private String diaChi;

    public KhachHang(String maKH, String hoTen, String gioiTinh, String soDT, String diaChi) {
        setMaKH(maKH);
        setHoTen(hoTen);
        setGioiTinh(gioiTinh);
        setSoDT(soDT);
        setDiaChi(diaChi);
    }

    public KhachHang() { this("", "", "", "", ""); }
    public KhachHang(String maKH) { this(maKH, "", "", "", ""); }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) {
        if (maKH == null || maKH.isEmpty()) throw new IllegalArgumentException("Mã KH rỗng!");
        this.maKH = maKH;
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

	@Override
	public String toString() {
		return "KhachHang [maKH=" + maKH + ", hoTen=" + hoTen + ", gioiTinh=" + gioiTinh + ", soDT=" + soDT
				+ ", diaChi=" + diaChi + "]";
	}

}
