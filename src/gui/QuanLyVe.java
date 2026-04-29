package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectDB;
import dao.QuanLyCTHD_DAO;
import dao.QuanLyVe_DAO;
import entity.ChiTietHoaDon;
import entity.LoadData;
import entity.Ve;
import entity.ResetForm;

public class QuanLyVe extends JPanel implements LoadData, ResetForm {
    private QuanLyVe_DAO ticketManager;
    private QuanLyCTHD_DAO cthdManager;

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaVe, txtMaGhe, txtNgayBan, txtMaSuatChieu, txtDaThanhToan, txtTimVe;
    private JButton btnXoa, btnXoaRong, btnLuu, btnTim;

    private ArrayList<Ve> danhSach = new ArrayList<>();

    public QuanLyVe() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ======= KẾT NỐI DATABASE =======
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            if (con != null)
                System.out.println("Kết nối SQL Server thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }

        this.ticketManager = new QuanLyVe_DAO();
        this.cthdManager = new QuanLyCTHD_DAO();

        JPanel pnNorth = new JPanel(new GridBagLayout());
        pnNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Thông tin vé phim",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 20),
                Color.DARK_GRAY));
        pnNorth.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaVe = new JLabel("Mã vé:");
        JLabel lblGhe = new JLabel("Mã ghế:");
        JLabel lblNgayBan = new JLabel("Ngày bán:");
        JLabel lblMaSuatChieu = new JLabel("Mã suất chiếu:");
        JLabel lblDaThanhToan = new JLabel("Trạng thái:");

        Font lblFont = new Font("Segoe UI", Font.BOLD, 18);
        for (JLabel lbl : new JLabel[] { lblMaVe, lblGhe, lblNgayBan, lblMaSuatChieu, lblDaThanhToan })
            lbl.setFont(lblFont);

        this.txtMaVe = new JTextField(20);
        this.txtMaGhe = new JTextField(20);
        this.txtNgayBan = new JTextField(20);
        this.txtMaSuatChieu = new JTextField(20);
        this.txtDaThanhToan = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnNorth.add(lblMaVe, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtMaVe, gbc);
        gbc.gridx = 2;
        pnNorth.add(lblGhe, gbc);
        gbc.gridx = 3;
        pnNorth.add(txtMaGhe, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnNorth.add(lblNgayBan, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtNgayBan, gbc);
        gbc.gridx = 2;
        pnNorth.add(lblMaSuatChieu, gbc);
        gbc.gridx = 3;
        pnNorth.add(txtMaSuatChieu, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnNorth.add(lblDaThanhToan, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtDaThanhToan, gbc);
        gbc.gridx = 2;
        pnNorth.add(new JLabel(""), gbc);
        gbc.gridx = 3;
        pnNorth.add(new JLabel(""), gbc);

        setFormEditable(false);
        add(pnNorth, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "STT", "Mã vé", "Mã ghế", "Ngày bán", "Mã suất chiếu", "Trạng thái"
        }, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Danh sách vé",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                Color.DARK_GRAY));
        add(scroll, BorderLayout.CENTER);

        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnSouth.setBackground(Color.WHITE);

        JLabel lblTim = new JLabel("Xem vé phim:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 18));
        this.txtTimVe = new JTextField(15);
        this.txtTimVe.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        btnTim = new JButton("Xem");
        btnXoa = new JButton("Xóa");
        btnXoaRong = new JButton("Xóa rỗng");
        btnLuu = new JButton("Lưu");

        JButton[] arrBtns = { btnXoa, btnXoaRong, btnLuu, btnTim };
        Color[] colors = {
                new Color(46, 204, 113), // xanh lá
                new Color(52, 152, 219), // xanh dương
                new Color(231, 76, 60), // đỏ
                new Color(155, 89, 182), // tím
        };

        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);
        for (int i = 0; i < arrBtns.length; i++) {
            arrBtns[i].setFont(btnFont);
            arrBtns[i].setBackground(colors[i]);
            arrBtns[i].setForeground(Color.WHITE);
            arrBtns[i].setFocusPainted(false);
            arrBtns[i].setPreferredSize(new Dimension(130, 45));
        }

        pnSouth.add(lblTim);
        pnSouth.add(this.txtTimVe);
        pnSouth.add(btnTim);
        pnSouth.add(btnXoa);
        pnSouth.add(btnXoaRong);
        pnSouth.add(btnLuu);

        add(pnSouth, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        loadData();

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> resetForm());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());
    }

    // Load data from DAO into table
    @Override
    public void loadData() {
        danhSach = this.ticketManager.getDanhSachVe();
        refreshTable();
    }

    private void refreshTable() {
        this.model.setRowCount(0);
        if (danhSach == null)
            return;
        int count = 1;
        for (Ve ve : danhSach) {
            String stt = Integer.toString(count);
            String maVe = ve.getMaVe();
            String maGhe = ve.getGhe().getMaGhe();
            String ngayBan = ve.getNgayBan().toString();
            String maSuatChieu = ve.getMaSuatChieu();
            String trangThai = ve.getTrangThai();

            this.model.addRow(new Object[] { stt, maVe, maGhe, ngayBan, maSuatChieu, trangThai });
            count++;
        }
    }

    private void xoaHoaDon() {
        String ma = this.txtMaVe.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã vé cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa vé phim này " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (this.ticketManager.XoaVeTheoMa(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy vé cần xóa!");
            }
        }
    }

    private void setFormEditable(boolean editable) {
        this.txtMaVe.setEditable(editable);
        this.txtDaThanhToan.setEditable(editable);
        this.txtMaGhe.setEditable(editable);
        this.txtMaSuatChieu.setEditable(editable);
        this.txtNgayBan.setEditable(editable);
    }

    private void timHoaDon() {
        String ma = this.txtTimVe.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã vé cần tìm!");
            return;
        }
        ChiTietHoaDon cthd = this.cthdManager.TimCTHDTheoMaVe(ma);
        if (cthd != null) {
            ArrayList<String> danhSachGhe = new ArrayList<>();
            danhSachGhe.add(cthd.getVe().getGhe().getTenGhe());
            new ThongTinVeModal(cthd.getHoaDon(), cthd.getVe(), this, danhSachGhe);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy vé có mã " + ma);
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < this.danhSach.size()) {
            Ve p = this.danhSach.get(i);
            this.txtMaVe.setText(p.getMaVe());
            this.txtTimVe.setText(p.getMaVe());
            this.txtMaGhe.setText(p.getGhe().getMaGhe());
            this.txtNgayBan.setText(p.getNgayBan().toString());
            this.txtMaSuatChieu.setText(p.getMaSuatChieu());
            this.txtDaThanhToan.setText(String.valueOf(p.getTrangThai()));
        }
    }

    @Override
    public void resetForm() {
        this.txtMaVe.setText("");
        this.txtMaGhe.setText("");
        this.txtNgayBan.setText("");
        this.txtMaSuatChieu.setText("");
        this.txtDaThanhToan.setText("");
        this.txtTimVe.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu vào CSDL!");
    }

}
