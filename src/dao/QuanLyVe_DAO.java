package dao;

import java.util.ArrayList;
import java.util.Random;

import ConnectDB.ConnectDB;

import java.sql.*;
import java.time.LocalDate;

import entity.Ve;

public class QuanLyVe_DAO {
    private Connection conn;

    public QuanLyVe_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public boolean add(Ve ve) {
        if (ve == null || this.conn == null)
            return false;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "Insert into Ve(maVe, maGhe, ngayBan, maSuatChieu, daThanhToan) "
                    + "values(?,?,?,?,?)";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, ve.getMaVe());
            stmt.setString(2, ve.getGhe().getMaGhe());
            stmt.setDate(3, Date.valueOf(ve.getNgayBan()));
            stmt.setString(4, ve.getMaSuatChieu());
            stmt.setBoolean(5, ve.isDaThanhToan());
            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public Ve findVeByID(String maVe) {
        if (this.conn == null || maVe == null || maVe.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Ve ve = null;
        try {
            String sql = "Select * from Ve where maVe = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maVe);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String maGhe = rs.getString("maGhe");
                LocalDate ngayBan = rs.getDate("ngayBan").toLocalDate();
                String maSuatChieu = rs.getString("maSuatChieu");
                boolean daThanhToan = rs.getBoolean("daThanhToan");
                QuanLyGhe_DAO gheManager = new QuanLyGhe_DAO();
                ve = new Ve(maVe, gheManager.TimGheTheoMa(maGhe), ngayBan, maSuatChieu, daThanhToan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return ve;
    }
    //Tìm danh sách vé theo mã suất chiếu
    public ArrayList<Ve> timVeTheoMaSuatChieu(String maSuatChieu) {
        if (this.conn == null || maSuatChieu == null || maSuatChieu.trim().isEmpty())
            return null;
        ArrayList<Ve> ticketList = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from Ve where maSuatChieu = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maSuatChieu);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maVe = rs.getString("maVe");
                String maGhe = rs.getString("maGhe");
                LocalDate ngayBan = rs.getDate("ngayBan").toLocalDate();
                boolean daThanhToan = rs.getBoolean("daThanhToan");
                QuanLyGhe_DAO gheManager = new QuanLyGhe_DAO();
                Ve ve = new Ve(maVe, gheManager.TimGheTheoMa(maGhe), ngayBan, maSuatChieu, daThanhToan);
                ticketList.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return ticketList;
    }
    public static String taoMaVeTuDong() {
        long timeMillis = System.currentTimeMillis();
        int rand = new Random().nextInt(1000);
        return "VE" + timeMillis + String.format("%03d", rand);
    }

    public ArrayList<Ve> getDanhSachVe() {
        if (this.conn == null)
            return null;
        ArrayList<Ve> dsVe = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from Ve";
            stmt = this.conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maVe = rs.getString("maVe");
                String maGhe = rs.getString("maGhe");
                LocalDate ngayBan = rs.getDate("ngayBan").toLocalDate();
                String maSuatChieu = rs.getString("maSuatChieu");
                boolean daThanhToan = rs.getBoolean("daThanhToan");
                QuanLyGhe_DAO gheManager = new QuanLyGhe_DAO();
                Ve ve = new Ve(maVe, gheManager.TimGheTheoMa(maGhe), ngayBan, maSuatChieu, daThanhToan);
                dsVe.add(ve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return dsVe;
    }

    public boolean XoaVeTheoMa(String maVe) {
        if (this.conn == null)
            return false;

        PreparedStatement stmtChiTiet = null;
        PreparedStatement stmtVe = null;
        int n = 0;

        try {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Xóa trước trong ChiTietHoaDon
            String sqlCTHD = "DELETE FROM ChiTietHoaDon WHERE maVe = ?";
            stmtChiTiet = conn.prepareStatement(sqlCTHD);
            stmtChiTiet.setString(1, maVe);
            stmtChiTiet.executeUpdate();

            // 2. Sau đó xóa trong Ve
            String sqlVe = "DELETE FROM Ve WHERE maVe = ?";
            stmtVe = conn.prepareStatement(sqlVe);
            stmtVe.setString(1, maVe);
            n = stmtVe.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            close(null, stmtChiTiet);
            close(null, stmtVe);
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return n > 0;
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
