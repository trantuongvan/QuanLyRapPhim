package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import entity.SuatChieu;

public class QuanLySuatChieu_DAO {
    private Connection conn;

    public QuanLySuatChieu_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public boolean addNewSuatChieu(SuatChieu suatChieu) {
        if (this.conn == null || suatChieu == null)
            return false;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "Insert into SuatChieu(maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe) "
                    + "values(?,?,?,?,?,?)";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, suatChieu.getMaSuatChieu());
            stmt.setString(2, suatChieu.getMaPhim());
            stmt.setString(3, suatChieu.getMaRap());
            stmt.setDate(4, Date.valueOf(suatChieu.getNgayChieu()));
            stmt.setTime(5, Time.valueOf(suatChieu.getGioChieu()));
            stmt.setDouble(6, suatChieu.getGiaVe());
            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public SuatChieu timSuatChieu(String maSuatChieu) {
        if (this.conn == null || maSuatChieu == null || maSuatChieu.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SuatChieu suatChieu = null;
        try {
            String sql = "Select * from SuatChieu where maSuatChieu = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maSuatChieu);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String maPhim = rs.getString("maPhim");
                String maRap = rs.getString("maRap");
                LocalDate ngayChieu = rs.getDate("ngayChieu").toLocalDate();
                LocalTime gioChieu = rs.getTime("gioChieu").toLocalTime();
                float giaVe = rs.getFloat("giaVe");
                suatChieu = new SuatChieu(maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return suatChieu;
    }

    public ArrayList<SuatChieu> getAllSuatChieu() {
        if (this.conn == null)
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<SuatChieu> danhSachSuatChieu = new ArrayList<>();
        try {
            String sql = "Select * from SuatChieu";
            stmt = this.conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maSuatChieu = rs.getString("maSuatChieu");
                String maPhim = rs.getString("maPhim");
                String maRap = rs.getString("maRap");
                LocalDate ngayChieu = rs.getDate("ngayChieu").toLocalDate();
                LocalTime gioChieu = rs.getTime("gioChieu").toLocalTime();
                float giaVe = rs.getFloat("giaVe");
                SuatChieu suatChieu = new SuatChieu(maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe);
                danhSachSuatChieu.add(suatChieu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachSuatChieu;
    }

    public ArrayList<SuatChieu> getSuatChieuTheoPhim(String maPhim) {
        if (this.conn == null || maPhim == null || maPhim.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<SuatChieu> danhSachSuatChieu = new ArrayList<>();
        try {
            String sql = "Select * from SuatChieu where maPhim = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maSuatChieu = rs.getString("maSuatChieu");
                String maRap = rs.getString("maRap");
                LocalDate ngayChieu = rs.getDate("ngayChieu").toLocalDate();
                LocalTime gioChieu = rs.getTime("gioChieu").toLocalTime();
                float giaVe = rs.getFloat("giaVe");
                SuatChieu suatChieu = new SuatChieu(maSuatChieu, maPhim, maRap, ngayChieu, gioChieu, giaVe);
                danhSachSuatChieu.add(suatChieu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachSuatChieu;
    }

    // Xóa suất chiếu
    public boolean removeSuatChieu(SuatChieu suatChieu) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "DELETE FROM SuatChieu WHERE maSuatChieu=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, suatChieu.getMaSuatChieu());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    // cập nhật suất chiếu
    public boolean updateSuatChieu(SuatChieu suatChieu) {
        if (this.conn == null || suatChieu == null)
            return false;

        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "UPDATE SuatChieu SET maPhim=?, maRap=?, ngayChieu=?, gioChieu=?, giaVe=? WHERE maSuatChieu=?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, suatChieu.getMaPhim());
            stmt.setString(2, suatChieu.getMaRap());
            stmt.setDate(3, Date.valueOf(suatChieu.getNgayChieu()));
            stmt.setTime(4, Time.valueOf(suatChieu.getGioChieu()));
            stmt.setDouble(5, suatChieu.getGiaVe());
            stmt.setString(6, suatChieu.getMaSuatChieu());
            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    // Tân comment: bỏ chức năng lưu luôn đi mình add là add trực tiếp vô database
    // luôn
    // Lưu vào database
    public boolean saveToDatabase() {
        // Giả lập lưu vào database
        System.out.println("Lưu suất chiếu vào database thành công!");
        return true;
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
