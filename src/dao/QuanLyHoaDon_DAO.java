package dao;

import java.util.ArrayList;
import java.util.Random;

import ConnectDB.ConnectDB;

import java.sql.*;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

public class QuanLyHoaDon_DAO {
    private Connection conn;

    public QuanLyHoaDon_DAO() {
        this.conn = ConnectDB.getConnection();
    }

    public boolean add(HoaDon hoaDon) {
        if (this.conn == null || hoaDon == null)
            return false;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            String sql = "Insert into HoaDon(maHoaDon, ngayLap, maNV, maKH, tongTien) VALUES (?,?,?,?,?)";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, hoaDon.getMaHoaDon());
            stmt.setDate(2, Date.valueOf(hoaDon.getNgayLap()));
            stmt.setString(3, hoaDon.getNhanVien().getMaNV());
            stmt.setString(4, hoaDon.getKhachHang().getMaKH());
            stmt.setFloat(5, hoaDon.getTongTien());
            n = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, stmt);
        }
        return n > 0;
    }

    public HoaDon findHoaDonByID(String maHoaDon) {
        if (this.conn == null || maHoaDon == null || maHoaDon.trim().isEmpty())
            return null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HoaDon hoaDon = null;
        try {
            String sql = "Select * from HoaDon where maHoaDon = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Date ngayLapDate = rs.getDate("ngayLap");
                String maNV = rs.getString("maNV");
                String maKH = rs.getString("maKH");
                float tongTien = rs.getFloat("tongTien");

                QuanLyNhanVien_DAO nhanVienDAO = new QuanLyNhanVien_DAO();
                QuanLyKhachHang_DAO khachHangDAO = new QuanLyKhachHang_DAO();
                NhanVien nv = nhanVienDAO.timTheoMa(maNV);
                KhachHang kh = khachHangDAO.findKhachHang(maKH);
                hoaDon = new HoaDon(maHoaDon, ngayLapDate.toLocalDate(), nv, kh, tongTien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return hoaDon;
    }

    public static String taoMaHoaDonTuDong() {
        long timeMillis = System.currentTimeMillis();
        int rand = new Random().nextInt(1000);
        return "HD" + timeMillis + String.format("%03d", rand);
    }

    public ArrayList<HoaDon> getDanhSachHoaDon() {
        if (this.conn == null)
            return null;
        ArrayList<HoaDon> danhSachHoaDon = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from HoaDon";
            stmt = this.conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                Date ngayLapDate = rs.getDate("ngayLap");
                String maNV = rs.getString("maNV");
                String maKH = rs.getString("maKH");
                float tongTien = rs.getFloat("tongTien");

                QuanLyNhanVien_DAO nhanVienDAO = new QuanLyNhanVien_DAO();
                QuanLyKhachHang_DAO khachHangDAO = new QuanLyKhachHang_DAO();
                NhanVien nv = nhanVienDAO.timTheoMa(maNV);
                KhachHang kh = khachHangDAO.findKhachHang(maKH);
                HoaDon hoaDon = new HoaDon(maHoaDon, ngayLapDate.toLocalDate(), nv, kh, tongTien);
                danhSachHoaDon.add(hoaDon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return danhSachHoaDon;
    }

    public boolean XoaHoaDonTheoMa(String maHD) {
        if (this.conn == null)
            return false;

        PreparedStatement stmtCTHD = null;
        PreparedStatement stmtHD = null;
        int n = 0;

        try {
            // Bắt đầu transaction
            conn.setAutoCommit(false);

            // 1️⃣ Xóa các chi tiết hóa đơn liên quan
            String sqlCTHD = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
            stmtCTHD = conn.prepareStatement(sqlCTHD);
            stmtCTHD.setString(1, maHD);
            stmtCTHD.executeUpdate();

            // 2️⃣ Xóa hóa đơn chính
            String sqlHD = "DELETE FROM HoaDon WHERE maHoaDon = ?";
            stmtHD = conn.prepareStatement(sqlHD);
            stmtHD.setString(1, maHD);
            n = stmtHD.executeUpdate();

            // 3️⃣ Xác nhận (commit) nếu không có lỗi
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            close(null, stmtCTHD);
            close(null, stmtHD);
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

    // Tường thêm 2 hàm tính doanh thu & số lượng vé theo phim
    public double tinhDoanhThuTheoPhim(String maPhim) {
        if (this.conn == null || maPhim == null || maPhim.trim().isEmpty())
            return 0;
        double doanhThu = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // HoaDon không lưu trực tiếp maSuatChieu. Đi qua ChiTietHoaDon -> Ve ->
            // SuatChieu
            String sql = "SELECT SUM(ct.soLuong * ct.giaVe) AS TongDoanhThu " +
                    "FROM ChiTietHoaDon ct " +
                    "JOIN Ve v ON ct.maVe = v.maVe " +
                    "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                    "WHERE sc.maPhim = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();
            if (rs.next()) {
                doanhThu = rs.getDouble("TongDoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return doanhThu;
    }

    public int tinhTongSoLuongVeTheoPhim(String maPhim) {
        if (this.conn == null || maPhim == null || maPhim.trim().isEmpty())
            return 0;
        int tongSoLuongVe = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Đếm số vé từ bảng Ve liên kết qua SuatChieu
            String sql = "SELECT COUNT(v.maVe) AS TongSoLuongVe " +
                    "FROM Ve v JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                    "WHERE sc.maPhim = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maPhim);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tongSoLuongVe = rs.getInt("TongSoLuongVe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return tongSoLuongVe;
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
    // số lượng vé của 1 Hóa Đơn
    public int getTongSoLuongVeCuaHoaDon(String maHoaDon) {
        int tongSoVe = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SUM(soLuong) AS TongVe FROM ChiTietHoaDon WHERE maHoaDon = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tongSoVe = rs.getInt("TongVe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, stmt);
        }
        return tongSoVe;
    }
}
