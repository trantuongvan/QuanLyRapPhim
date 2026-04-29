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
                boolean tinhTrang = rs.getBoolean("tinhTrang");
                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                ghe = new Ghe(maGhe, tenGhe, rap, tinhTrang);
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
                boolean tinhTrang = rs.getBoolean("tinhTrang");
                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                ghe = new Ghe(maGhe, tenGhe, rap, tinhTrang);
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
                boolean tinhTrang = rs.getBoolean("tinhTrang");
                QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
                Rap rap = rapManager.findRapByID(maRap);
                Ghe ghe = new Ghe(maGhe, tenGhe, rap, tinhTrang);
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
                boolean tinhTrang = rs.getBoolean("tinhTrang");
                Ghe ghe = new Ghe(maGhe, tenGhe, rap, tinhTrang);
                danhSachGhe.add(ghe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachGhe;
    }

    public boolean capNhatTinhTrangGhe(Ghe ghe) {
        if (this.conn == null || ghe == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String sql = "Update Ghe set tinhTrang = ? where maGhe = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setBoolean(1, ghe.isDaDat());
            stmt.setString(2, ghe.getMaGhe());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return false;
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
