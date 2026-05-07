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
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmtNV = null;
        PreparedStatement stmtTK = null;
        try {
            con.setAutoCommit(false);

            String sqlNV = "INSERT INTO NhanVien (maNV, tenNV, diaChi, soDienThoai, ngaySinh, email, gioiTinh) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmtNV = con.prepareStatement(sqlNV);
            stmtNV.setString(1, nv.getMaNV());
            stmtNV.setString(2, nv.getTenNV());
            stmtNV.setString(3, nv.getDiaChi());
            stmtNV.setString(4, nv.getSoDienThoai());
            stmtNV.setDate(5, java.sql.Date.valueOf(nv.getNgaySinh()));
            stmtNV.setString(6, nv.getEmail());
            stmtNV.setString(7, nv.getGioiTinh());
            stmtNV.executeUpdate();

            String sqlTK = "INSERT INTO TaiKhoan (maNV, taiKhoan, matKhau, vaiTro) VALUES (?, ?, ?, ?)";
            stmtTK = con.prepareStatement(sqlTK);
            stmtTK.setString(1, nv.getMaNV());
            stmtTK.setString(2, nv.getMaNV());
            stmtTK.setString(3, "123456");
            stmtTK.setString(4, "NHAN_VIEN");
            stmtTK.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
                if (stmtNV != null) stmtNV.close();
                if (stmtTK != null) stmtTK.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
