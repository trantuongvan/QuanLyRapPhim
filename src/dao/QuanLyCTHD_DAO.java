package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;

public class QuanLyCTHD_DAO {
    private Connection conn;

    public QuanLyCTHD_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    /**
     * THÊM CHI TIẾT HÓA ĐƠN VÀO CƠ SỞ DỮ LIỆU
     * Đây là phương thức bạn đang thiếu
     */
    public boolean add(ChiTietHoaDon cthd) {
        if (this.conn == null || cthd == null) return false;

        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maVe, soLuong, giaVe) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getVe().getMaVe());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setDouble(4, cthd.getGiaVe()); // Lưu ý: Đảm bảo tên thuộc tính trong entity là giaTien hoặc giaVe khớp với getter

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm Chi Tiết Hóa Đơn dựa trên mã vé (Sử dụng cho chức năng tìm kiếm vé)
     */
    public ChiTietHoaDon TimCTHDTheoMaVe(String maVe) {
        if (this.conn == null || maVe == null || maVe.trim().isEmpty())
            return null;

        ChiTietHoaDon cthd = null;
        QuanLyVe_DAO veManager = new QuanLyVe_DAO();
        QuanLyHoaDon_DAO hoaDonManager = new QuanLyHoaDon_DAO();

        String sql = "SELECT maHoaDon, maVe, soLuong, giaVe FROM ChiTietHoaDon WHERE maVe = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maVe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maHD = rs.getString("maHoaDon");
                    int soLuong = rs.getInt("soLuong");
                    double giaVe = rs.getDouble("giaVe");

                    // Lấy thông tin thực thể liên quan
                    Ve ve = veManager.findVeByID(maVe);
                    HoaDon hd = hoaDonManager.findHoaDonByID(maHD);

                    cthd = new ChiTietHoaDon(hd, ve, soLuong, giaVe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cthd;
    }

    /**
     * Tìm danh sách CTHD theo mã hóa đơn
     */
    public ArrayList<ChiTietHoaDon> timCTHDTheoMaHoaDon(String maHoaDon, HoaDon currentHoaDon) {
        ArrayList<ChiTietHoaDon> dsCTHD = new ArrayList<>();
        if (this.conn == null || maHoaDon == null) return dsCTHD;

        QuanLyVe_DAO veManager = new QuanLyVe_DAO();
        String sql = "SELECT maVe, soLuong, giaVe FROM ChiTietHoaDon WHERE maHoaDon = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maVe = rs.getString("maVe");
                    int soLuong = rs.getInt("soLuong");
                    double giaVe = rs.getDouble("giaVe");

                    Ve ve = veManager.findVeByID(maVe);
                    dsCTHD.add(new ChiTietHoaDon(currentHoaDon, ve, soLuong, giaVe));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCTHD;
    }

    public ArrayList<ChiTietHoaDon> timCTHDTheoMaHoaDon(String maHoaDon) {
        QuanLyHoaDon_DAO hoaDonManager = new QuanLyHoaDon_DAO();
        HoaDon hd = hoaDonManager.findHoaDonByID(maHoaDon);
        return timCTHDTheoMaHoaDon(maHoaDon, hd);
    }
}