package entity;

import java.time.LocalDate;

public class Ve {
    private String maVe;
    private Ghe ghe;
    private LocalDate ngayBan;
    private String maSuatChieu;
    private boolean daThanhToan;

    public Ve(String maVe) {
        setMaVe(maVe);
        this.ngayBan = LocalDate.now();
        this.daThanhToan = false;
    }
     public Ve(String maVe, Ghe ghe, LocalDate ngayBan, String maSuatChieu, boolean daThanhToan) {
        setMaVe(maVe);
        setGhe(ghe);
        setNgayBan(ngayBan);
        setMaSuatChieu(maSuatChieu);
        setDaThanhToan(daThanhToan);
    }
    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        if (maVe == null || maVe.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã vé không được rỗng!");
        }
        this.maVe = maVe;
    }

    public Ghe getGhe() {
        return ghe;
    }

    public void setGhe(Ghe ghe) {
        this.ghe = ghe;
    }

    public LocalDate getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(LocalDate ngayBan) {
        this.ngayBan = ngayBan;
    }

    public String getMaSuatChieu() {
        return maSuatChieu;
    }

    public void setMaSuatChieu(String maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public boolean isDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }
    public String getTrangThai(){
        return isDaThanhToan() ? "Đã thanh toán" : "Chưa thanh toán";
    }
    @Override
    public String toString() {
        return "Vé {" +
                "Mã vé='" + maVe + '\'' +
                ", Ghế=" + (ghe != null ? ghe.getMaGhe() : "Chưa chọn") +
                ", Ngày bán=" + ngayBan +
                ", Mã suất chiếu='" + maSuatChieu + '\'' +
                ", Đã thanh toán=" + daThanhToan +
                '}';
    }
}
