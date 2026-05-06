package entity;

import java.time.LocalDate;

public class HoaDon {
	private String maHoaDon;
	private LocalDate ngayLap;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	private float tongTien; // Thêm tổng tiền

	public HoaDon() {
		this("", LocalDate.now(), null, null, 0);
	}

	public HoaDon(String maHoaDon, LocalDate ngayLap, NhanVien nhanVien, KhachHang khachHang,
			float tongTien) {
		setKhachHang(khachHang);
		setMaHoaDon(maHoaDon);
		setNgayLap(ngayLap);
		setNhanVien(nhanVien);
		setTongTien(tongTien);
	}

	public HoaDon(String maHoaDon) {
		super();
		this.maHoaDon = maHoaDon;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public LocalDate getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public float getTongTien() {
		return tongTien;
	}

	public void setTongTien(float tongTien) {
		this.tongTien = tongTien;
	}


	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", nhanVien=" + nhanVien + ", khachHang="
				+ khachHang + ", tongtien=" + tongTien + "]";
	}

}
