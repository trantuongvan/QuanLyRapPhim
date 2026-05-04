package dao;

import java.sql.*;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import entity.NhanVien;

public class QuanLyNhanVien_DAO {
    
    public static ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhanVien";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("diaChi"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("email"),
                    rs.getString("gioiTinh")
                );
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean tonTaiMaNV(String maNV) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT 1 FROM NhanVien WHERE maNV = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // có dòng → đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themNhanVien(NhanVien nv) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nv.getMaNV());
            stmt.setString(2, nv.getTenNV());
            stmt.setString(3, nv.getDiaChi());
            stmt.setString(4, nv.getSoDienThoai());
            stmt.setDate(5, java.sql.Date.valueOf(nv.getNgaySinh()));
            stmt.setString(6, nv.getEmail());
            stmt.setString(7, nv.getGioiTinh());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE NhanVien SET tenNV=?, diaChi=?, soDienThoai=?, ngaySinh=?, email=?, gioiTinh=? WHERE maNV=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nv.getTenNV());
            stmt.setString(2, nv.getDiaChi());
            stmt.setString(3, nv.getSoDienThoai());
            stmt.setDate(4, java.sql.Date.valueOf(nv.getNgaySinh()));
            stmt.setString(5, nv.getEmail());
            stmt.setString(6, nv.getGioiTinh());
            stmt.setString(7, nv.getMaNV());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaNhanVien(String maNV) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "DELETE FROM NhanVien WHERE maNV=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<NhanVien> timTheoTen(String tenNV) {
        ArrayList<NhanVien> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE tenNV LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tenNV + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("diaChi"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("email"),
                    rs.getString("gioiTinh")
                );
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
	
	//Tìm nhân viên theo mã
	public NhanVien timTheoMa(String maNV) {
    NhanVien nv = null;
    try {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, maNV);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            nv = new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("diaChi"),
                rs.getString("soDienThoai"),
                rs.getDate("ngaySinh").toLocalDate(),
                rs.getString("email"),
                rs.getString("gioiTinh")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return nv;
}

}
