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
import entity.VaiTro;

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
    private JButton btnThem, btnSua, btnXoa, btnTaiLai, btnTim;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QuanLyNhanVien() {
        daoNV = new QuanLyNhanVien_DAO();
        
        // ====== SETUP PANEL CHÍNH ======
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(0, 0));
        
        // ====== PANEL CONTENT CHÍNH - CĂN GIỮA ======
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(new Color(245, 245, 245));
        pnlContent.setBorder(new EmptyBorder(20, 50, 20, 50)); // Margin xung quanh
        add(pnlContent, BorderLayout.CENTER);

        // ====== TIÊU ĐỀ ======
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(new Color(245, 245, 245));
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Tamoha", Font.BOLD, 32));
        lblTitle.setForeground(new Color(220, 0, 0));
        pnlTitle.add(lblTitle);
        pnlContent.add(pnlTitle, BorderLayout.NORTH);

        // ====== PANEL CHỨA FORM VÀ BẢNG ======
        JPanel pnlMainContent = new JPanel(new BorderLayout(15, 15));
        pnlMainContent.setBackground(new Color(245, 245, 245));
        pnlContent.add(pnlMainContent, BorderLayout.CENTER);

        // ====== FORM NHẬP LIỆU ======
        JPanel pnlForm = createFormPanel();
        pnlMainContent.add(pnlForm, BorderLayout.NORTH);

        // ====== BẢNG DỮ LIỆU ======
        JScrollPane scrollTable = createTable();
        pnlMainContent.add(scrollTable, BorderLayout.CENTER);

        // ====== PANEL NÚT CHỨC NĂNG ======
        JPanel pnlButtons = createButtonPanel();
        pnlContent.add(pnlButtons, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
        attachEvents();
        loadNhanVien();
    }

    private JPanel createFormPanel() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        pnlForm.setPreferredSize(new Dimension(0, 200)); // Chiều cao cố định

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Font chung
        Font labelFont = new Font("Tamoha", Font.BOLD, 14);
        Font fieldFont = new Font("Tamoha", Font.PLAIN, 14);

        int row = 0;

        // Hàng 1: Mã NV và Tên NV
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1;
        JLabel lblMa = new JLabel("Mã NV:");
        lblMa.setFont(labelFont);
        pnlForm.add(lblMa, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtMa = createStyledTextField();
        txtMa.setFont(fieldFont);
        pnlForm.add(txtMa, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.1;
        JLabel lblTen = new JLabel("Tên NV:");
        lblTen.setFont(labelFont);
        pnlForm.add(lblTen, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtTen = createStyledTextField();
        txtTen.setFont(fieldFont);
        pnlForm.add(txtTen, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);
        pnlForm.add(lblGioiTinh, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.4;
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(fieldFont);
        cboGioiTinh.setBackground(Color.WHITE);
        cboGioiTinh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        pnlForm.add(cboGioiTinh, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.1;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        pnlForm.add(lblNgaySinh, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtNgaySinh = createStyledTextField();
        txtNgaySinh.setFont(fieldFont);
        txtNgaySinh.setToolTipText("Định dạng: dd/MM/yyyy");
        pnlForm.add(txtNgaySinh, gbc);
        row++;

        // Hàng 3: SĐT và Email
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1;
        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setFont(labelFont);
        pnlForm.add(lblSdt, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtSdt = createStyledTextField();
        txtSdt.setFont(fieldFont);
        pnlForm.add(txtSdt, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        pnlForm.add(lblEmail, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtEmail = createStyledTextField();
        txtEmail.setFont(fieldFont);
        pnlForm.add(txtEmail, gbc);
        row++;

        // Hàng 4: Địa chỉ
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(labelFont);
        pnlForm.add(lblDiaChi, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.9;
        txtDiaChi = createStyledTextField();
        txtDiaChi.setFont(fieldFont);
        pnlForm.add(txtDiaChi, gbc);

        return pnlForm;
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 235, 235));
        table.setSelectionForeground(Color.BLACK);
        
        // Header table
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 0, 0));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scroll.setPreferredSize(new Dimension(0, 300)); // Chiều cao ưu tiên
        
        return scroll;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(new Color(245, 245, 245));
        pnlButtons.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(new Color(245, 245, 245));
        JLabel lblTim = new JLabel("Tìm theo tên:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlSearch.add(lblTim);
        
        txtTim = createStyledTextField();
        txtTim.setPreferredSize(new Dimension(200, 35));
        txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(txtTim);
        
        btnTim = createStyledButton("Tìm kiếm", new Color(0, 102, 204));
        pnlSearch.add(btnTim);

        // Các nút chức năng
        JPanel pnlFunctionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlFunctionButtons.setBackground(new Color(245, 245, 245));
        
        btnThem = createStyledButton("Thêm mới", new Color(40, 167, 69));
        btnSua = createStyledButton("Cập nhật", new Color(255, 193, 7));
        btnXoa = createStyledButton("Xóa", new Color(220, 53, 69));
        btnTaiLai = createStyledButton("Làm mới", new Color(108, 117, 125));

        pnlFunctionButtons.add(btnThem);
        pnlFunctionButtons.add(btnSua);
        pnlFunctionButtons.add(btnXoa);
        pnlFunctionButtons.add(btnTaiLai);

        // Thêm vào panel chính
        pnlButtons.add(pnlSearch);
        pnlButtons.add(Box.createRigidArea(new Dimension(0, 10))); // Khoảng cách
        pnlButtons.add(pnlFunctionButtons);

        return pnlButtons;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txt.setBackground(Color.WHITE);
        return txt;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker()),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    private void attachEvents() {
        btnTaiLai.addActionListener(e -> loadNhanVien());
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnTim.addActionListener(e -> timNhanVien());
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                hienThiLenForm();
            }
        });

        // Double click để sửa
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
                JOptionPane.showMessageDialog(this, "✅ Thêm nhân viên thành công!");
                loadNhanVien();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể thêm nhân viên!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Lỗi: " + ex.getMessage());
        }
    }

    private void suaNhanVien() {
        try {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn nhân viên cần sửa!");
                return;
            }
            NhanVien nv = layDuLieuForm();
            if (daoNV.capNhatNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVien();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể cập nhật!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Lỗi: " + ex.getMessage());
        }
    }

    private void xoaNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn nhân viên cần xóa!");
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
                JOptionPane.showMessageDialog(this, "🗑️ Đã xóa thành công!");
                loadNhanVien();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể xóa nhân viên này!");
            }
        }
    }

    private void timNhanVien() {
        String keyword = txtTim.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập tên cần tìm!");
            txtTim.requestFocus();
            return;
        }
        model.setRowCount(0);
        ArrayList<NhanVien> ds = daoNV.timTheoTen(keyword);
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "🔍 Không tìm thấy nhân viên nào!");
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