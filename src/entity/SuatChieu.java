package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class SuatChieu {
    private String maSuatChieu;
    private String maPhim;
    private String maRap;
    private LocalDate ngayChieu;
    private LocalTime gioChieu;

    private float giaVe;

    public SuatChieu(String maSuatChieu) {
        setMaSuatChieu(maSuatChieu);
    }

    public SuatChieu(String maSuatChieu, String maPhim, String maRap, LocalDate ngayChieu, LocalTime gioChieu,
            float giaVe) {
        setMaSuatChieu(maSuatChieu);
        setMaPhim(maPhim);
        setMaRap(maRap);
        setNgayChieu(ngayChieu);
        setGioChieu(gioChieu);
        setGiaVe(giaVe);
    }

    public String getMaSuatChieu() {
        return maSuatChieu;
    }

    public void setMaSuatChieu(String maSuatChieu) {
        if (maSuatChieu == null || maSuatChieu.trim().isEmpty())
            throw new IllegalArgumentException("Mã suất chiếu không được rỗng!");
        this.maSuatChieu = maSuatChieu.trim();
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = (maPhim != null) ? maPhim.trim() : null;
    }

    public String getMaRap() {
        return maRap;
    }

    public void setMaRap(String maRap) {
        this.maRap = (maRap != null) ? maRap.trim() : null;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalTime getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(LocalTime gioChieu) {
        this.gioChieu = gioChieu;
    }

    public float getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(float giaVe) {
        this.giaVe = giaVe;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SuatChieu))
            return false;
        SuatChieu other = (SuatChieu) obj;
        return maSuatChieu.equals(other.maSuatChieu);
    }

    @Override
    public String toString() {
        return "SuatChieu{" +
                "maSuatChieu='" + maSuatChieu + '\'' +
                ", maPhim='" + maPhim + '\'' +
                ", maRap='" + maRap + '\'' +
                ", ngayChieu=" + ngayChieu +
                ", ngioChieu=" + gioChieu +
                ", giaVe=" + giaVe +
                '}';
    }
}
