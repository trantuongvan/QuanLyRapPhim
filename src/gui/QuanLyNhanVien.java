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

    @Override
    public void loadData() {
        loadNhanVien();
    }

    private JTextField txtMa, txtTen, txtDiaChi, txtSdt, txtEmail, txtNgaySinh, txtTim;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnSua, btnXoa, btnTaiLai, btnTim, btnXoaTrang;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 16);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 16);
    private final Color orangeColor = new Color(245, 140, 0);

    public QuanLyNhanVien() {
        daoNV = new QuanLyNhanVien_DAO();

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(new Color(34, 34, 34));

        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnCenterTop = new JPanel();
        pnCenterTop.setLayout(new BoxLayout(pnCenterTop, BoxLayout.Y_AXIS));
        pnCenterTop.setOpaque(false);

        JPanel pnTop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnTop.setOpaque(false);
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.X_AXIS));
        pnTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Giữ chiều cao cố định

        txtTim = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(orangeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        txtTim.setOpaque(false);
        txtTim.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTim.setFont(FONT_TXT);
        txtTim.setForeground(Color.BLACK);
        txtTim.setCaretColor(Color.BLACK);
        txtTim.setMaximumSize(new Dimension(500, 35));

        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaTrang = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnTaiLai = taoNutBoGoc("Làm mới", orangeColor);

        pnTop.add(txtTim);
        pnTop.add(Box.createHorizontalGlue());
        pnTop.add(btnTim); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnThem); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnSua); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoa); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoaTrang); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnTaiLai);

        pnCenterTop.add(pnTop);
        pnCenterTop.add(Box.createVerticalStrut(20));

        JPanel pnInput = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.white);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnInput.setOpaque(false);
        pnInput.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblThongTin = new JLabel("THÔNG TIN NHÂN VIÊN");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblThongTin.setForeground(Color.BLACK);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        pnInput.add(lblThongTin, gbc);

        JTextField[] tfs = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            tfs[i] = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(orangeColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                }
            };
            tfs[i].setOpaque(false);
            tfs[i].setBorder(new EmptyBorder(0, 10, 0, 10));
            tfs[i].setForeground(Color.BLACK);
            tfs[i].setFont(FONT_TXT);
            tfs[i].setCaretColor(Color.BLACK);
            tfs[i].setPreferredSize(new Dimension(200, 35));
        }

        txtMa = tfs[0]; txtTen = tfs[1]; txtDiaChi = tfs[2];
        txtEmail = tfs[3]; txtNgaySinh = tfs[4]; txtSdt = tfs[5];

        cboGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
        cboGioiTinh.setBackground(orangeColor);
        cboGioiTinh.setForeground(Color.BLACK);
        cboGioiTinh.setFont(FONT_TXT);
        cboGioiTinh.setPreferredSize(new Dimension(200, 35));

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Mã nhân viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtMa, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Họ tên:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(cboGioiTinh, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Ngày sinh:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(txtNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        pnInput.add(taoLabel("Sđt:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.5;
        pnInput.add(txtSdt, gbc);
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        pnInput.add(taoLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.5;
        pnInput.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        pnInput.add(taoLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0; gbc.gridwidth = 3;
        pnInput.add(txtDiaChi, gbc);

        pnCenterTop.add(pnInput);

        add(pnCenterTop, BorderLayout.NORTH);

        JScrollPane scrollTable = createTable();
        add(scrollTable, BorderLayout.CENTER);

        // sự kiện
        attachEvents();
        loadNhanVien();
    }

    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LBL);
        lbl.setForeground(Color.BLACK);
        return lbl;
    }

    private JButton taoNutBoGoc(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
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
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setMaximumSize(new Dimension(110, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JScrollPane createTable() {
        String[] cols = {"Mã NV", "Tên NV", "Giới tính", "Ngày sinh", "SĐT", "Email", "Địa chỉ"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));

        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Tạo khoảng cách với khối form ở trên
        return scroll;
    }

    private void attachEvents() {
        btnTaiLai.addActionListener(e -> loadNhanVien());
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnTim.addActionListener(e -> timNhanVien());
        btnXoaTrang.addActionListener(e -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                hienThiLenForm();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    hienThiLenForm();
                }
            }
        });
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
        txtMa.setText("");
        txtTen.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtMa.requestFocus();
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