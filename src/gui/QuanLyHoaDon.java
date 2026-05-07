package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
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

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 18);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    private ArrayList<HoaDon> danhSach = new ArrayList<>();

    public QuanLyHoaDon() {
        this.billManager = new QuanLyHoaDon_DAO();

        // ======= KẾT NỐI DATABASE =======
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

        // --- TIÊU ĐỀ ---
        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        // --- THANH CÔNG CỤ (TOP) ---
        gbc.gridy = 1;
        JPanel pnTop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnTop.setOpaque(false);
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnTop.setPreferredSize(new Dimension(0, 70));

        txtTimHD = createStyledTextField(300);
        btnTim = taoNutBoGoc("Xem", new Color(160, 82, 45));
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLuu = taoNutBoGoc("Lưu", orangeColor);

        JLabel lblTim = new JLabel("Tìm hóa đơn: ");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim);
        pnTop.add(txtTimHD);
        pnTop.add(btnTim);
        pnTop.add(btnXoa);
        pnTop.add(btnXoaRong);
        pnTop.add(btnLuu);
        add(pnTop, gbc);

        // --- NHẬP LIỆU (CENTER) ---
        gbc.gridy = 2;
        JPanel pnInput = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnInput.setOpaque(false);
        pnInput.setLayout(new GridBagLayout());

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 20, 10, 20);
        innerGbc.anchor = GridBagConstraints.WEST;

        txtMaHD = createStyledTextField(200);
        txtNgayLap = createStyledTextField(200);
        txtMaNV = createStyledTextField(200);
        txtMaKH = createStyledTextField(200);
        txtSoLuongVe = createStyledTextField(200);
        txtTongTien = createStyledTextField(200);

        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.gridwidth = 4;
        innerGbc.anchor = GridBagConstraints.CENTER;
        JLabel lblHeader = new JLabel("THÔNG TIN CHI TIẾT");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        pnInput.add(lblHeader, innerGbc);

        innerGbc.gridwidth = 1;
        innerGbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Mã hóa đơn:", "Ngày lập:", "Mã nhân viên:", "Mã khách hàng:", "Số lượng vé:", "Tổng tiền:"};
        JLabel[] jLabels = new JLabel[labels.length];
        for (int i = 0; i < labels.length; i++) {
            jLabels[i] = new JLabel(labels[i]);
            jLabels[i].setForeground(Color.WHITE);
            jLabels[i].setFont(new Font("Tahoma", Font.BOLD, 14));
        }

        // Hàng 1
        innerGbc.gridy = 1;
        innerGbc.gridx = 0; pnInput.add(jLabels[0], innerGbc);
        innerGbc.gridx = 1; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtMaHD, innerGbc);
        innerGbc.gridx = 2; innerGbc.fill = GridBagConstraints.NONE; innerGbc.weightx = 0; pnInput.add(jLabels[1], innerGbc);
        innerGbc.gridx = 3; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtNgayLap, innerGbc);

        // Hàng 2
        innerGbc.gridy = 2;
        innerGbc.gridx = 0; innerGbc.fill = GridBagConstraints.NONE; innerGbc.weightx = 0; pnInput.add(jLabels[2], innerGbc);
        innerGbc.gridx = 1; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtMaNV, innerGbc);
        innerGbc.gridx = 2; innerGbc.fill = GridBagConstraints.NONE; innerGbc.weightx = 0; pnInput.add(jLabels[3], innerGbc);
        innerGbc.gridx = 3; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtMaKH, innerGbc);

        // Hàng 3
        innerGbc.gridy = 3;
        innerGbc.gridx = 0; innerGbc.fill = GridBagConstraints.NONE; innerGbc.weightx = 0; pnInput.add(jLabels[4], innerGbc);
        innerGbc.gridx = 1; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtSoLuongVe, innerGbc);
        innerGbc.gridx = 2; innerGbc.fill = GridBagConstraints.NONE; innerGbc.weightx = 0; pnInput.add(jLabels[5], innerGbc);
        innerGbc.gridx = 3; innerGbc.fill = GridBagConstraints.HORIZONTAL; innerGbc.weightx = 0.5; pnInput.add(txtTongTien, innerGbc);

        add(pnInput, gbc);

        // --- BẢNG DỮ LIỆU (BOTTOM) ---
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        model = new DefaultTableModel(new String[]{"Mã hóa đơn", "Ngày lập", "Mã NV", "Mã KH", "Số lượng vé", "Tổng tiền"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(35);

        table.setBackground(tableBackground);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 70, 70));
        table.setSelectionBackground(new Color(80, 80, 80));
        table.setSelectionForeground(orangeColor);

        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(tableBackground);
        add(scroll, gbc);

        // SỰ KIỆN
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> resetForm());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());

        loadData();
    }

    private JTextField createStyledTextField(int width) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(orangeColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(0, 10, 0, 10));
        tf.setFont(FONT_TXT);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setPreferredSize(new Dimension(width, 35));
        return tf;
    }

    private JButton taoNutBoGoc(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHovered ? bgColor.darker() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    @Override
    public void loadData() {
        danhSach = billManager.getDanhSachHoaDon();
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (HoaDon hd : danhSach) {
            int sl = billManager.getTongSoLuongVeCuaHoaDon(hd.getMaHoaDon());
            model.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getNgayLap(),
                    hd.getNhanVien().getMaNV(),
                    hd.getKhachHang().getMaKH(),
                    sl,
                    hd.getTongTien()
            });
        }
    }

    private void xoaHoaDon() {
        String ma = txtMaHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (billManager.XoaHoaDonTheoMa(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                resetForm();
            }
        }
    }

    private void timHoaDon() {
        String ma = txtTimHD.getText().trim();
        if (ma.isEmpty()) return;
        HoaDon hd = billManager.findHoaDonByID(ma);
        if (hd != null) new HoaDonModal(hd, this);
        else JOptionPane.showMessageDialog(this, "Không tìm thấy!");
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < danhSach.size()) {
            HoaDon p = danhSach.get(i);
            txtMaHD.setText(p.getMaHoaDon());
            txtTimHD.setText(p.getMaHoaDon());
            txtNgayLap.setText(p.getNgayLap().toString());
            txtMaNV.setText(p.getNhanVien().getMaNV());
            txtMaKH.setText(p.getKhachHang().getMaKH());
            txtSoLuongVe.setText(table.getValueAt(i, 4).toString());
            txtTongTien.setText(String.valueOf(p.getTongTien()));
        }
    }

    @Override
    public void resetForm() {
        txtMaHD.setText("");
        txtNgayLap.setText("");
        txtMaNV.setText("");
        txtMaKH.setText("");
        txtSoLuongVe.setText("");
        txtTongTien.setText("");
        txtTimHD.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu!");
    }
}