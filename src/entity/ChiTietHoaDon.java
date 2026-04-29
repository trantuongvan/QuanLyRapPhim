package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private int soLuong;
    private double giaVe;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, Ve ve, int soLuong, double giaVe) {
        setGiaVe(giaVe);
        setHoaDon(hoaDon);
        setSoLuong(soLuong);
        setVe(ve);
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Ve getVe() {
        return ve;
    }

    public void setVe(Ve ve) {
        this.ve = ve;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public double tinhThanhTien() {
        return giaVe * soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "hoaDon=" + (hoaDon != null ? hoaDon.getMaHoaDon() : "null") +
                ", Ve=" + ve.getMaVe() +
                ", so luong=" + soLuong +
                ", giaVe=" + giaVe +
                '}';
    }
}
