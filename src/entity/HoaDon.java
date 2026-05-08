package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
	private String maHoaDon;
	private LocalDate ngayLap;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	private float tongTien;

	// This is the missing field that caused the error
	private List<Ve> danhSachVe;

	public HoaDon() {
		this("", LocalDate.now(), null, null, 0);
		this.danhSachVe = new ArrayList<>();
	}

	public HoaDon(String maHoaDon, LocalDate ngayLap, NhanVien nhanVien, KhachHang khachHang, float tongTien) {
		setKhachHang(khachHang);
		setMaHoaDon(maHoaDon);
		setNgayLap(ngayLap);
		setNhanVien(nhanVien);
		setTongTien(tongTien);
		this.danhSachVe = new ArrayList<>();
	}

	public HoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
		this.danhSachVe = new ArrayList<>();
	}

	// --- GETTERS AND SETTERS ---

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

	// THE MISSING METHOD
	public List<Ve> getDanhSachVe() {
		return danhSachVe;
	}

	public void setDanhSachVe(List<Ve> danhSachVe) {
		this.danhSachVe = danhSachVe;
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", nhanVien=" + nhanVien + ", khachHang="
				+ khachHang + ", tongtien=" + tongTien + "]";
	}
}