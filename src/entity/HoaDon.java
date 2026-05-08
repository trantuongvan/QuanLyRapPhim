package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLap;
    private NhanVien nhanVien;
    private KhachHang khachHang;

    // Đổi danhSachVe thành danhSachCTHD để phục vụ việc tính tiền
    private List<ChiTietHoaDon> danhSachCTHD;

    public HoaDon() {
        this("", LocalDate.now(), null, null);
    }

    // Đã xóa tham số tongTien ra khỏi Constructor
    public HoaDon(String maHoaDon, LocalDate ngayLap, NhanVien nhanVien, KhachHang khachHang) {
        setKhachHang(khachHang);
        setMaHoaDon(maHoaDon);
        setNgayLap(ngayLap);
        setNhanVien(nhanVien);
        this.danhSachCTHD = new ArrayList<>();
    }

    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
        this.danhSachCTHD = new ArrayList<>();
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

    public List<ChiTietHoaDon> getDanhSachCTHD() {
        return danhSachCTHD;
    }

    public void setDanhSachCTHD(List<ChiTietHoaDon> danhSachCTHD) {
        this.danhSachCTHD = danhSachCTHD;
    }

    public double getTongTien() {
        double tong = 0;
        if (this.danhSachCTHD != null) {
            for (ChiTietHoaDon cthd : danhSachCTHD) {
                tong += cthd.tinhThanhTien();
            }
        }
        return tong;
    }

    public List<Ve> getDanhSachVe() {
        List<Ve> dsVe = new ArrayList<>();
        if (this.danhSachCTHD != null) {
            for (ChiTietHoaDon cthd : this.danhSachCTHD) {
                if (cthd.getVe() != null) {
                    dsVe.add(cthd.getVe());
                }
            }
        }
        return dsVe;
    }

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", nhanVien=" + nhanVien + ", khachHang="
                + khachHang + ", tongTien=" + getTongTien() + "]";
    }
}