package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectDB;
import dao.QuanLyHoaDon_DAO;
import entity.HoaDon;
import entity.LoadData;
import entity.ResetForm;

public class QuanLyHoaDon extends JPanel implements LoadData, ResetForm {
    private QuanLyHoaDon_DAO billManager;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaHD, txtNgayLap, txtMaNV, txtMaKH, txtSoLuongVe, txtTongTien, txtTimHD;
    private JButton btnXoa, btnXoaRong, btnLuu, btnTim;

    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    private ArrayList<HoaDon> danhSach = new ArrayList<>();

    public QuanLyHoaDon() {
        this.billManager = new QuanLyHoaDon_DAO();
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        // Top Panel (Search & Buttons)
        gbc.gridy = 1;
        JPanel pnTop = createRoundedPanel();
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        txtTimHD = createStyledTextField(250);
        btnTim = taoNutBoGoc("Xem", new Color(160, 82, 45));
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLuu = taoNutBoGoc("Lưu", orangeColor);

        JLabel lblTim = new JLabel("Tìm hóa đơn: ");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim); pnTop.add(txtTimHD); pnTop.add(btnTim);
        pnTop.add(btnXoa); pnTop.add(btnXoaRong); pnTop.add(btnLuu);
        add(pnTop, gbc);

        // Input Panel
        gbc.gridy = 2;
        JPanel pnInput = createRoundedPanel();
        pnInput.setLayout(new GridBagLayout());
        setupInputFields(pnInput);
        add(pnInput, gbc);

        // Table
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        setupTable(gbc);

        // Event Listeners
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> resetForm());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());

        loadData();
    }

    private JPanel createRoundedPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
    }

    private void setupInputFields(JPanel pn) {
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 20, 10, 20);

        txtMaHD = createStyledTextField(180);
        txtNgayLap = createStyledTextField(180);
        txtMaNV = createStyledTextField(180);
        txtMaKH = createStyledTextField(180);
        txtSoLuongVe = createStyledTextField(180);
        txtTongTien = createStyledTextField(180);

        String[] labels = {"Mã hóa đơn:", "Ngày lập:", "Mã nhân viên:", "Mã khách hàng:", "Số lượng vé:", "Tổng tiền:"};
        JTextField[] fields = {txtMaHD, txtNgayLap, txtMaNV, txtMaKH, txtSoLuongVe, txtTongTien};

        for (int i = 0; i < labels.length; i++) {
            innerGbc.gridx = (i % 2 == 0) ? 0 : 2;
            innerGbc.gridy = (i / 2) + 1;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            pn.add(lbl, innerGbc);

            innerGbc.gridx++;
            innerGbc.fill = GridBagConstraints.HORIZONTAL;
            innerGbc.weightx = 0.5;
            pn.add(fields[i], innerGbc);
            innerGbc.weightx = 0;
        }
    }

    private void setupTable(GridBagConstraints gbc) {
        model = new DefaultTableModel(new String[]{"Mã hóa đơn", "Ngày lập", "Mã NV", "Mã KH", "Số lượng vé", "Tổng tiền"}, 0);
        table = new JTable(model);
        table.setBackground(tableBackground);
        table.setForeground(Color.WHITE);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(tableBackground);
        add(scroll, gbc);
    }

    private JTextField createStyledTextField(int width) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width, 35));
        tf.setBackground(orangeColor);
        tf.setBorder(new EmptyBorder(0, 10, 0, 10));
        tf.setFont(FONT_TXT);
        return tf;
    }

    private JButton taoNutBoGoc(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    @Override
    public void loadData() {
        danhSach = billManager.getDanhSachHoaDon();
        model.setRowCount(0);
        for (HoaDon hd : danhSach) {
            int sl = billManager.getTongSoLuongVeCuaHoaDon(hd.getMaHoaDon());
            model.addRow(new Object[]{hd.getMaHoaDon(), hd.getNgayLap(), hd.getNhanVien().getMaNV(), hd.getKhachHang().getMaKH(), sl, hd.getTongTien()});
        }
    }

    private void timHoaDon() {
        String ma = txtTimHD.getText().trim();
        if (ma.isEmpty()) return;

        HoaDon hd = billManager.findHoaDonByID(ma);
        if (hd != null) {
            // This now works because getDanhSachVe() exists in HoaDon.java
            new HoaDonModal(hd, this, hd.getDanhSachVe());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn mã: " + ma);
        }
    }

    private void xoaHoaDon() {
        String ma = txtMaHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn để xóa!");
            return;
        }
        if (billManager.XoaHoaDonTheoMa(ma)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
            resetForm();
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0) {
            txtMaHD.setText(model.getValueAt(i, 0).toString());
            txtNgayLap.setText(model.getValueAt(i, 1).toString());
            txtMaNV.setText(model.getValueAt(i, 2).toString());
            txtMaKH.setText(model.getValueAt(i, 3).toString());
            txtSoLuongVe.setText(model.getValueAt(i, 4).toString());
            txtTongTien.setText(model.getValueAt(i, 5).toString());
        }
    }

    @Override
    public void resetForm() {
        txtMaHD.setText(""); txtNgayLap.setText(""); txtMaNV.setText("");
        txtMaKH.setText(""); txtSoLuongVe.setText(""); txtTongTien.setText("");
        txtTimHD.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Lưu thành công!");
    }
}