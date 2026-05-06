package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import ConnectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;

public class DangNhap_DAO {
	public TaiKhoan ktDangNhap(String taiKhoan, String matKhau) {
		Connection con = ConnectDB.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TaiKhoan tkhoan = null;
		try {
			String sql = "SELECT K.maNV, K.taiKhoan, K.matKhau, K.vaiTro, " +
					"V.tenNV, V.diaChi, V.soDienThoai, V.ngaySinh, V.email, V.gioiTinh " +
					"FROM TaiKhoan K JOIN NhanVien V ON K.maNV = V.maNV " +
					"WHERE K.taiKhoan = ? and K.matKhau = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, taiKhoan);
			stmt.setString(2, matKhau);
			rs = stmt.executeQuery();
			if (rs.next()) {
				// Lấy dữ liệu theo tên cột
				String maNV = rs.getString("maNV");
				String tenDN = rs.getString("taiKhoan");
				String mk = rs.getString("matKhau");
				String tenNV = rs.getString("tenNV");
				String diaChi = rs.getString("diaChi");
				String sdt = rs.getString("soDienThoai");
				LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
				String email = rs.getString("email");
				String gioiTinh = rs.getString("gioiTinh");

                String role = rs.getString("vaiTro");
                VaiTro vaiTro = VaiTro.valueOf(role);

				NhanVien nvien = new NhanVien(maNV, tenNV, diaChi, sdt, ngaySinh, email, gioiTinh);
				tkhoan = new TaiKhoan(nvien, tenDN, mk, vaiTro);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tkhoan;
	}
}
