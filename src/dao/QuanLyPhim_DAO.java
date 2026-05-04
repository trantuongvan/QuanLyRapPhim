package dao;

import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;
import entity.Phim;
import entity.TheLoaiPhim;

public class QuanLyPhim_DAO {

    public ArrayList<Phim> getAllPhim() {
        ArrayList<Phim> dsPhim = new ArrayList<>();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM Phim";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maPhim = rs.getString("maPhim");
                String tenPhim = rs.getString("tenPhim");
                String nhaSanXuat = rs.getString("nhaSanXuat");
                String theLoaiStr = rs.getString("theLoai");
                TheLoaiPhim theLoai = TheLoaiPhim.fromTenHienThi(theLoaiStr); 
                int thoiLuong = rs.getInt("thoiLuong");
                String quocGia = rs.getString("quocGia");

                Phim p = new Phim(maPhim, tenPhim, nhaSanXuat, theLoai, thoiLuong, quocGia);
                dsPhim.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return dsPhim;
    }

    public boolean themPhim(Phim p) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "INSERT INTO Phim(maPhim, tenPhim, nhaSanXuat, theLoai, thoiLuong, quocGia) VALUES(?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getMaPhim());
            stmt.setString(2, p.getTenPhim());
            stmt.setString(3, p.getNhaSanXuat());
            stmt.setString(4, p.getTheLoai().getTenHienThi()); 
            stmt.setInt(5, p.getThoiLuong());
            stmt.setString(6, p.getQuocGia());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public boolean capNhatPhim(Phim p) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "UPDATE Phim SET tenPhim=?, nhaSanXuat=?, theLoai=?, thoiLuong=?, quocGia=? WHERE maPhim=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getTenPhim());
            stmt.setString(2, p.getNhaSanXuat());
            stmt.setString(3, p.getTheLoai().getTenHienThi()); 
            stmt.setInt(4, p.getThoiLuong());
            stmt.setString(5, p.getQuocGia());
            stmt.setString(6, p.getMaPhim());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public boolean phimDaDuocSuDung(String maPhim) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM SuatChieu WHERE maPhim=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }

        return false;
    }

    public boolean xoaPhim(String maPhim) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "DELETE FROM Phim WHERE maPhim=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhim);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public Phim timPhimTheoMa(String maPhim) {
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Phim p = null;
        try {
            String sql = "SELECT * FROM Phim WHERE maPhim=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String theLoaiStr = rs.getString("theLoai");
                TheLoaiPhim theLoai = TheLoaiPhim.fromTenHienThi(theLoaiStr);

                p = new Phim(
                        rs.getString("maPhim"),
                        rs.getString("tenPhim"),
                        rs.getString("nhaSanXuat"),
                        theLoai,
                        rs.getInt("thoiLuong"),
                        rs.getString("quocGia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return p;
    }

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
