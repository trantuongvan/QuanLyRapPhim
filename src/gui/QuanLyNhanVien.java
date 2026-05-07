package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import dao.QuanLyNhanVien_DAO;
import entity.NhanVien;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import entity.LoadData;

public class QuanLyNhanVien extends JPanel implements LoadData {
    private QuanLyNhanVien_DAO daoNV;
    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtDiaChi, txtSdt, txtEmail, txtNgaySinh, txtTim;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnSua, btnXoa, btnTaiLai, btnTim, btnXoaTrang;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    public QuanLyNhanVien() {
        daoNV = new QuanLyNhanVien_DAO();

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Title ---
        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        // --- Top Search & Control Panel ---
        gbc.gridy = 1;
        JPanel pnTop = createStyledPanel();
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        txtTim = createStyledTextField(300);
        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaTrang = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnTaiLai = taoNutBoGoc("Làm mới", orangeColor);

        JLabel lblTim = new JLabel("Tìm tên: ");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim);
        pnTop.add(txtTim);
        pnTop.add(btnTim);
        pnTop.add(btnThem);
        pnTop.add(btnSua);
        pnTop.add(btnXoa);
        pnTop.add(btnXoaTrang);
        pnTop.add(btnTaiLai);
        add(pnTop, gbc);

        // --- Input Panel ---
        gbc.gridy = 2;
        JPanel pnInput = createStyledPanel();
        pnInput.setLayout(new GridBagLayout());
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(8, 20, 8, 20);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;

        // Header Label
        innerGbc.gridx = 0; innerGbc.gridy = 0;
        innerGbc.gridwidth = 4;
        JLabel lblHeader = new JLabel("THÔNG TIN CHI TIẾT");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        pnInput.add(lblHeader, innerGbc);

        // Reset width for inputs
        innerGbc.gridwidth = 1;
        innerGbc.weightx = 0.1;

        // Fields
        txtMa = createStyledTextField(200);
        txtTen = createStyledTextField(200);
        txtNgaySinh = createStyledTextField(200);
        txtSdt = createStyledTextField(200);
        txtEmail = createStyledTextField(200);
        txtDiaChi = createStyledTextField(200);

        cboGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
        cboGioiTinh.setBackground(orangeColor);
        cboGioiTinh.setFont(FONT_TXT);

        // Row 1: Ma NV & Ten NV
        addRow(pnInput, innerGbc, 1, "Mã nhân viên:", txtMa, "Họ tên:", txtTen);
        // Row 2: Gioi Tinh & Ngay Sinh
        addRow(pnInput, innerGbc, 2, "Giới tính:", cboGioiTinh, "Ngày sinh:", txtNgaySinh);
        // Row 3: SDT & Email
        addRow(pnInput, innerGbc, 3, "Số điện thoại:", txtSdt, "Email:", txtEmail);
        // Row 4: Dia Chi (Span)
        innerGbc.gridy = 4; innerGbc.gridx = 0;
        pnInput.add(createWhiteLabel("Địa chỉ:"), innerGbc);
        innerGbc.gridx = 1; innerGbc.gridwidth = 3;
        pnInput.add(txtDiaChi, innerGbc);

        add(pnInput, gbc);

        // --- Table ---
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        String[] cols = {"Mã NV", "Tên NV", "Giới tính", "Ngày sinh", "SĐT", "Email", "Địa chỉ"};
        model = new DefaultTableModel(cols, 0);
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

        attachEvents();
        loadData();
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private JLabel createWhiteLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        return lbl;
    }

    private void addRow(JPanel p, GridBagConstraints c, int row, String l1, JComponent c1, String l2, JComponent c2) {
        c.gridy = row;
        c.gridx = 0; p.add(createWhiteLabel(l1), c);
        c.gridx = 1; c.weightx = 0.5; p.add(c1, c);
        c.gridx = 2; c.weightx = 0.1; p.add(createWhiteLabel(l2), c);
        c.gridx = 3; c.weightx = 0.5; p.add(c2, c);
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
        model.setRowCount(0);
        ArrayList<NhanVien> ds = daoNV.getAllNhanVien();
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getGioiTinh(),
                    nv.getNgaySinh().format(fmt), nv.getSoDienThoai(),
                    nv.getEmail(), nv.getDiaChi()
            });
        }
    }

    private void attachEvents() {
        btnTaiLai.addActionListener(e -> loadData());
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnTim.addActionListener(e -> timNhanVien());
        btnXoaTrang.addActionListener(e -> clearForm());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { hienThiLenForm(); }
        });
    }

    private void hienThiLenForm() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMa.setText(model.getValueAt(row, 0).toString());
            txtTen.setText(model.getValueAt(row, 1).toString());
            cboGioiTinh.setSelectedItem(model.getValueAt(row, 2).toString());
            txtNgaySinh.setText(model.getValueAt(row, 3).toString());
            txtSdt.setText(model.getValueAt(row, 4).toString());
            txtEmail.setText(model.getValueAt(row, 5).toString());
            txtDiaChi.setText(model.getValueAt(row, 6).toString());
        }
    }

    private void loadNhanVien() {
        model.setRowCount(0);
        ArrayList<NhanVien> ds = daoNV.getAllNhanVien();
        for (NhanVien nv : ds) {
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getGioiTinh(),
                    nv.getNgaySinh().format(fmt), nv.getSoDienThoai(),
                    nv.getEmail(), nv.getDiaChi()
            });
        }
    }

    private void themNhanVien() {
        try {
            NhanVien nv = layDuLieuForm();

            if (daoNV.tonTaiMaNV(nv.getMaNV())) {
                JOptionPane.showMessageDialog(this,
                        "Mã nhân viên đã tồn tại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (daoNV.themNhanVien(nv)) {
                String thongBao = "Thêm nhân viên thành công!\n"
                        + "Tài khoản đăng nhập đã được tạo:\n"
                        + "- Tên đăng nhập: " + nv.getMaNV() + "\n"
                        + "- Mật khẩu: 123456";
                JOptionPane.showMessageDialog(this, thongBao, "Hệ thống thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadNhanVien();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void suaNhanVien() {
        try {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
                return;
            }
            NhanVien nv = layDuLieuForm();
            if (daoNV.capNhatNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVien();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void xoaNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        String maNV = model.getValueAt(row, 0).toString();
        String tenNV = model.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa nhân viên:\n" + tenNV + " (" + maNV + ")?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (daoNV.xoaNhanVien(maNV)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadNhanVien();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này!");
            }
        }
    }

    private void timNhanVien() {
        String keyword = txtTim.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên cần tìm!");
            txtTim.requestFocus();
            return;
        }
        model.setRowCount(0);
        ArrayList<NhanVien> ds = daoNV.timTheoTen(keyword);
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!");
        } else {
            for (NhanVien nv : ds) {
                model.addRow(new Object[]{
                        nv.getMaNV(), nv.getTenNV(), nv.getGioiTinh(),
                        nv.getNgaySinh().format(fmt), nv.getSoDienThoai(),
                        nv.getEmail(), nv.getDiaChi()
                });
            }
        }
    }



    private void clearForm() {
        txtMa.setText(""); txtTen.setText(""); txtDiaChi.setText("");
        txtSdt.setText(""); txtEmail.setText(""); txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
    }

    private NhanVien layDuLieuForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSdt.getText().trim();
        String email = txtEmail.getText().trim();
        String gioiTinh = cboGioiTinh.getSelectedItem().toString();
        LocalDate ngaySinh = LocalDate.parse(txtNgaySinh.getText().trim(), fmt);

        if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin bắt buộc (Mã NV, Tên, SĐT)!");
        }

        return new NhanVien(ma, ten, diaChi, sdt, ngaySinh, email, gioiTinh);
    }
}