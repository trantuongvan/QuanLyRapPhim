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
import dao.QuanLyHoaDon_DAO;
import entity.HoaDon;
import entity.LoadData;
public class QuanLyHoaDon extends JPanel implements LoadData {
    private QuanLyHoaDon_DAO billManager = new QuanLyHoaDon_DAO();

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaHD, txtNgayLap, txtMaNV, txtMaKH, txtSoLuongVe, txtTongTien, txtTimHD;
    private JButton btnXoa, btnXoaRong, btnLuu, btnTim;

    private ArrayList<HoaDon> danhSach = new ArrayList<>();

    public QuanLyHoaDon() {
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

        billManager = new QuanLyHoaDon_DAO();

        JPanel pnNorth = new JPanel(new GridBagLayout());
        pnNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Thông tin hóa đơn",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tamoha", Font.BOLD, 20),
                Color.DARK_GRAY));
        pnNorth.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaHD = new JLabel("Mã hóa đơn:");
        JLabel lblNgayLap = new JLabel("Ngày lập:");
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        JLabel lblSoLuongVe = new JLabel("Số lượng vé:");
        JLabel lblTongTien = new JLabel("Tổng tiền:");

        Font lblFont = new Font("Tamoha", Font.BOLD, 18);
        for (JLabel lbl : new JLabel[] { lblMaHD, lblNgayLap, lblMaNV, lblMaKH, lblSoLuongVe, lblTongTien })
            lbl.setFont(lblFont);

        this.txtMaHD = new JTextField(20);
        this.txtNgayLap = new JTextField(20);
        this.txtMaNV = new JTextField(20);
        this.txtMaKH = new JTextField(20);
        this.txtSoLuongVe = new JTextField(20);
        this.txtTongTien = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnNorth.add(lblMaHD, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtMaHD, gbc);
        gbc.gridx = 2;
        pnNorth.add(lblNgayLap, gbc);
        gbc.gridx = 3;
        pnNorth.add(txtNgayLap, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnNorth.add(lblMaNV, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtMaNV, gbc);
        gbc.gridx = 2;
        pnNorth.add(lblMaKH, gbc);
        gbc.gridx = 3;
        pnNorth.add(txtMaKH, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnNorth.add(lblSoLuongVe, gbc);
        gbc.gridx = 1;
        pnNorth.add(txtSoLuongVe, gbc);
        gbc.gridx = 2;
        pnNorth.add(lblTongTien, gbc);
        gbc.gridx = 3;
        pnNorth.add(txtTongTien, gbc);

        setFormEditable(false);
        add(pnNorth, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Mã hóa đơn", "Ngày lập", "Mã NV", "Mã KH", "Số lượng vé", "Tổng tiền"
        }, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Danh sách hóa đơn",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                Color.DARK_GRAY));
        add(scroll, BorderLayout.CENTER);

        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnSouth.setBackground(Color.WHITE);

        JLabel lblTim = new JLabel("Xem hóa đơn:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 18));
        this.txtTimHD = new JTextField(15);
        this.txtTimHD.setFont(new Font("Segoe UI", Font.PLAIN, 18));

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
        pnSouth.add(this.txtTimHD);
        pnSouth.add(btnTim);
        pnSouth.add(btnXoa);
        pnSouth.add(btnXoaRong);
        pnSouth.add(btnLuu);

        add(pnSouth, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        loadData();

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> xoaRong());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());
    }

    // Load data from DAO into table
    @Override
    public void loadData() {
        danhSach = billManager.getDanhSachHoaDon();
        refreshTable();
    }

    private void refreshTable() {
        this.model.setRowCount(0);
        if (danhSach == null)
            return;
        for (HoaDon hd : danhSach) {
            String ma = hd.getMaHoaDon();
            String ngay = hd.getNgayLap() != null ? hd.getNgayLap().toString() : "";
            String maNV = hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "";
            String maKH = hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "";
//            int sl = hd.getSoLuongVe();
            float tt = hd.getTongTien();
            this.model.addRow(new Object[] { ma, ngay, maNV, maKH, tt });
        }
    }

    private void xoaHoaDon() {
        String ma = this.txtMaHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (this.billManager.XoaHoaDonTheoMa(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cần xóa!");
            }
        }
    }

    private void setFormEditable(boolean editable) {
        this.txtMaHD.setEditable(editable);
        this.txtNgayLap.setEditable(editable);
        this.txtMaNV.setEditable(editable);
        this.txtMaKH.setEditable(editable);
        this.txtSoLuongVe.setEditable(editable);
        this.txtTongTien.setEditable(editable);
    }

    private void timHoaDon() {
        String ma = this.txtTimHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn cần tìm!");
            return;
        }
        HoaDon hoaDon = this.billManager.findHoaDonByID(ma);
        if (hoaDon != null) {
            new HoaDonModal(hoaDon, this);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn có mã " + ma);
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < this.danhSach.size()) {
            HoaDon p = this.danhSach.get(i);
            this.txtMaHD.setText(p.getMaHoaDon());
            this.txtTimHD.setText(p.getMaHoaDon());
            ;
            this.txtNgayLap.setText(p.getNgayLap().toString());
            this.txtMaNV.setText(p.getNhanVien().getMaNV());
            this.txtMaKH.setText(p.getKhachHang().getMaKH());
//            this.txtSoLuongVe.setText(String.valueOf(p.getSoLuongVe()));
            this.txtTongTien.setText(Float.toString(p.getTongTien()));
        }
    }

    private void xoaRong() {
        txtMaHD.setText("");
        txtNgayLap.setText("");
        txtMaNV.setText("");
        txtMaKH.setText("");
        txtSoLuongVe.setText("");
        txtTongTien.setText("");
        this.txtTimHD.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu vào CSDL!");
    }

}
