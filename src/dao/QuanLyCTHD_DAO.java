package dao;

import java.util.ArrayList;

import ConnectDB.ConnectDB;

import java.sql.*;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;

public class QuanLyCTHD_DAO {
    private Connection conn;

    public QuanLyCTHD_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public void add(ChiTietHoaDon cthd) {
        if (conn == null || cthd == null)
            return;
        PreparedStatement stmt = null;
        try {
            String sql = "Insert into ChiTietHoaDon(maHoaDon, maVe, soLuong, giaVe) "
                    + "values(?,?,?,?)";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getVe().getMaVe());
            stmt.setString(3, Integer.toString(cthd.getSoLuong()));
            stmt.setString(4, Double.toString(cthd.getGiaVe()));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
    }

    // Lấy danh sách CTHD theo mã HoaDon
    public ArrayList<ChiTietHoaDon> timCTHDTheoMaHoaDon(String maHoaDon) {
        if (this.conn == null || maHoaDon == null || maHoaDon.trim().isEmpty())
            return null;
        ArrayList<ChiTietHoaDon> dsCTHD = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from ChiTietHoaDon where maHoaDon = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maVe = rs.getString("maVe");
                int soLuongVe = rs.getInt("soLuong");
                double giaVe = rs.getDouble("giaVe");

                QuanLyHoaDon_DAO hoaDonManager = new QuanLyHoaDon_DAO();
                QuanLyVe_DAO veManager = new QuanLyVe_DAO();

                ChiTietHoaDon cthd = new ChiTietHoaDon(
                        hoaDonManager.findHoaDonByID(maHoaDon),
                        veManager.findVeByID(maVe),
                        soLuongVe,
                        giaVe);
                dsCTHD.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return dsCTHD;
    }
    // Lấy CTHD theo mã vé
    public ChiTietHoaDon TimCTHDTheoMaVe(String maVe) {
        if (this.conn == null || maVe == null || maVe.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ChiTietHoaDon cthd = null;
        try {
            String sql = "Select * from ChiTietHoaDon where maVe = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maVe);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                int soLuong = rs.getInt("soLuong");
                float giaVe = rs.getFloat("giaVe");
                QuanLyVe_DAO ticketManager = new QuanLyVe_DAO();
                QuanLyHoaDon_DAO billManager = new QuanLyHoaDon_DAO();
                HoaDon hoaDon = billManager.findHoaDonByID(maHoaDon);
                Ve ve = ticketManager.findVeByID(maVe);
                cthd = new ChiTietHoaDon(hoaDon, ve, soLuong, giaVe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return cthd;
    }

    // ====== HÀM TIỆN ÍCH ======
    private void close(ResultSet rs, Statement stmt) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
