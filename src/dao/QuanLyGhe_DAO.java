package dao;

import java.util.ArrayList;

import ConnectDB.ConnectDB;

import java.sql.*;
import entity.Ghe;
import entity.Rap;

public class QuanLyGhe_DAO {
    private Connection conn;

    public QuanLyGhe_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public Ghe TimGheTheoMa(String maGhe) {
        if (this.conn == null || maGhe == null || maGhe.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Ghe ghe = null;
        try {
            String sql = "Select * from Ghe where maGhe = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maGhe);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String tenGhe = rs.getString("tenGhe");
                String maRap = rs.getString("maRap");

                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                ghe = new Ghe(maGhe, tenGhe, rap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return ghe;
    }

    public Ghe TimGheTheoTen(String tenGhe, String maRap) {
        if (this.conn == null || tenGhe == null || tenGhe.trim().isEmpty() || maRap == null || maRap.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Ghe ghe = null;
        try {
            String sql = "Select * from Ghe where tenGhe = ? and maRap = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, tenGhe);
            stmt.setString(2, maRap);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String maGhe = rs.getString("maGhe");

                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                ghe = new Ghe(maGhe, tenGhe, rap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return ghe;
    }

    public ArrayList<Ghe> getDanhSachTatCaGhe() {
        if (this.conn == null)
            return null;
        ArrayList<Ghe> danhSachGhe = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from Ghe";
            stmt = this.conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maGhe = rs.getString("maGhe");
                String tenGhe = rs.getString("tenGhe");
                String maRap = rs.getString("maRap");

                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                Ghe ghe = new Ghe(maGhe, tenGhe, rap);
                danhSachGhe.add(ghe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachGhe;
    }

    public ArrayList<Ghe> getDanhSachGheTheoRap(Rap rap) {
        if (this.conn == null || rap == null)
            return null;
        String maRap = rap.getMaRap();
        ArrayList<Ghe> danhSachGhe = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from Ghe where maRap = ? ORDER BY CAST(REPLACE(tenGhe, N'Ghế ', '') AS INT);";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maRap);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maGhe = rs.getString("maGhe");
                String tenGhe = rs.getString("tenGhe");

                Ghe ghe = new Ghe(maGhe, tenGhe, rap);
                danhSachGhe.add(ghe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachGhe;
    }

    // Đã xóa hàm capNhatTinhTrangGhe() vì không cần thiết nữa

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