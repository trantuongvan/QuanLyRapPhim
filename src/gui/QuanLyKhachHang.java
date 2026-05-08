package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import dao.QuanLyKhachHang_DAO;
import entity.KhachHang;
import entity.LoadData;
import entity.ResetForm;

public class QuanLyKhachHang extends JPanel implements ActionListener, MouseListener, LoadData, ResetForm {

    private QuanLyKhachHang_DAO kh_dao;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtMaKH, txtHoTen, txtSoDT, txtDiaChi, txtTimKiem;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnXoaTrang, btnXoa1Dong, btnLamMoi, btnSua, btnTimKiem;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 14);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    public QuanLyKhachHang() {
        kh_dao = new QuanLyKhachHang_DAO();

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- TITLE ---
        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        // --- TOP PANEL (SEARCH & ACTIONS) ---
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
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnTop.setPreferredSize(new Dimension(0, 70));

        txtTimKiem = createStyledTextField(300);
        btnTimKiem = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa1Dong = taoNutBoGoc("Xóa", orangeColor);
        btnXoaTrang = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        JLabel lblTim = new JLabel("Tìm kiếm: ");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim);
        pnTop.add(txtTimKiem);
        pnTop.add(btnTimKiem);
        pnTop.add(btnThem);
        pnTop.add(btnSua);
        pnTop.add(btnXoa1Dong);
        pnTop.add(btnXoaTrang);
        pnTop.add(btnLamMoi);
        add(pnTop, gbc);

        // --- INPUT PANEL ---
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

        // Subheader
        innerGbc.gridx = 0; innerGbc.gridy = 0;
        innerGbc.gridwidth = 4;
        JLabel lblHeader = new JLabel("THÔNG TIN CHI TIẾT");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        pnInput.add(lblHeader, innerGbc);

        innerGbc.gridwidth = 1;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: MaKH and HoTen
        innerGbc.gridy = 1; innerGbc.gridx = 0; innerGbc.weightx = 0;
        pnInput.add(createWhiteLabel("Mã khách hàng:"), innerGbc);
        innerGbc.gridx = 1; innerGbc.weightx = 0.5;
        txtMaKH = createStyledTextField(200);
        txtMaKH.setEditable(false);
        pnInput.add(txtMaKH, innerGbc);

        innerGbc.gridx = 2; innerGbc.weightx = 0;
        pnInput.add(createWhiteLabel("Họ tên:"), innerGbc);
        innerGbc.gridx = 3; innerGbc.weightx = 0.5;
        txtHoTen = createStyledTextField(200);
        pnInput.add(txtHoTen, innerGbc);

        // Row 2: GioiTinh and SoDT
        innerGbc.gridy = 2; innerGbc.gridx = 0; innerGbc.weightx = 0;
        pnInput.add(createWhiteLabel("Giới tính:"), innerGbc);
        innerGbc.gridx = 1; innerGbc.weightx = 0.5;
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setBackground(orangeColor);
        cboGioiTinh.setFont(new Font("Tahoma", Font.PLAIN, 16));
        pnInput.add(cboGioiTinh, innerGbc);

        innerGbc.gridx = 2; innerGbc.weightx = 0;
        pnInput.add(createWhiteLabel("Số điện thoại:"), innerGbc);
        innerGbc.gridx = 3; innerGbc.weightx = 0.5;
        txtSoDT = createStyledTextField(200);
        pnInput.add(txtSoDT, innerGbc);

        // Row 3: DiaChi (Span)
        innerGbc.gridy = 3; innerGbc.gridx = 0; innerGbc.weightx = 0;
        pnInput.add(createWhiteLabel("Địa chỉ:"), innerGbc);
        innerGbc.gridx = 1; innerGbc.gridwidth = 3; innerGbc.weightx = 1.0;
        txtDiaChi = createStyledTextField(0);
        pnInput.add(txtDiaChi, innerGbc);

        add(pnInput, gbc);

        // --- TABLE ---
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        String[] headers = {"Mã KH", "Họ tên", "Giới tính", "Số ĐT", "Địa chỉ"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
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

        // Events
        btnThem.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnXoa1Dong.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSua.addActionListener(this);
        btnTimKiem.addActionListener(this);
        table.addMouseListener(this);

        loadData();
    }

    private JLabel createWhiteLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(FONT_LBL);
        return lbl;
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
        if (width > 0) tf.setPreferredSize(new Dimension(width, 35));
        return tf;
    }

    private JButton taoNutBoGoc(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
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
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    @Override
    public void loadData() {
        tableModel.setRowCount(0);
        List<KhachHang> ds = kh_dao.getDanhSachKhachHang();
        if (ds != null) {
            for (KhachHang kh : ds) {
                tableModel.addRow(new Object[]{kh.getMaKH(), kh.getHoTen(), kh.getGioiTinh(), kh.getSoDT(), kh.getDiaChi()});
            }
        }
    }

    @Override
    public void resetForm() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSoDT.setText("");
        txtDiaChi.setText("");
        txtTimKiem.setText("");
        cboGioiTinh.setSelectedIndex(0);
        table.clearSelection();
        txtHoTen.requestFocus();
    }

    private boolean validateInput() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSoDT.getText().trim();
        if (hoTen.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và SĐT không được để trống!");
            return false;
        }
        return true;
    }

    private KhachHang layKhachHangTuForm() {
        String ma = txtMaKH.getText().trim().isEmpty() ? QuanLyKhachHang_DAO.taoMaKHTuDong() : txtMaKH.getText().trim();
        return new KhachHang(ma, txtHoTen.getText().trim(), cboGioiTinh.getSelectedItem().toString(), txtSoDT.getText().trim(), txtDiaChi.getText().trim());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(btnLamMoi)) loadData();
        else if (src.equals(btnXoaTrang)) resetForm();
        else if (src.equals(btnThem)) {
            if (validateInput()) {
                if (kh_dao.add(layKhachHangTuForm())) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadData();
                    resetForm();
                }
            }
        } else if (src.equals(btnSua)) {
            if (validateInput() && table.getSelectedRow() >= 0) {
                if (kh_dao.update(layKhachHangTuForm())) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                }
            }
        } else if (src.equals(btnXoa1Dong)) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String ma = tableModel.getValueAt(row, 0).toString();
                if (kh_dao.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                    loadData();
                    resetForm();
                }
            }
        } else if (src.equals(btnTimKiem)) {
            String target = txtTimKiem.getText().trim();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(target)) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    mouseClicked(null);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy!");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaKH.setText(tableModel.getValueAt(row, 0).toString());
            txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
            cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 2).toString());
            txtSoDT.setText(tableModel.getValueAt(row, 3).toString());
            txtDiaChi.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}