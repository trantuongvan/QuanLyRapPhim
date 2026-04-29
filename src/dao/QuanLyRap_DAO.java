package dao;

import java.sql.*;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import entity.Rap;

public class QuanLyRap_DAO {
    private Connection conn;

    public QuanLyRap_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public ArrayList<Rap> getAllRap() {
        if(this.conn == null)
            return null;
            
        ArrayList<Rap> danhSachhRap = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from Rap";
            stmt = this.conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maRap = rs.getString("maRap");
                String tenRap = rs.getString("tenRap");
                int soLuongGhe = rs.getInt("soLuongGhe");
                Rap rap = new Rap(maRap, soLuongGhe, tenRap);
                danhSachhRap.add(rap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachhRap;
    }

    public Rap findRapByID(String maRap) {
        if(this.conn == null || maRap == null || maRap.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Rap rap = null;
        try {
            String sql = "Select * from Rap where maRap = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maRap);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String tenRap = rs.getString("tenRap");
                int soLuongGhe = rs.getInt("soLuongGhe");
                rap = new Rap(maRap, soLuongGhe, tenRap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return rap;
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
