package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;

import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import dao.QuanLyRap_DAO;
import entity.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class QuanLySuatChieu extends JPanel implements ActionListener, LoadData {
    private QuanLySuatChieu_DAO quanLySuatChieu_DAO;
    private JTable tblSuatChieu;
    private JTextField txtMaSuat, txtNgayChieu, txtThoiGian, txtGiaVe, txtTimSuat, txtMaPhim;
    private JComboBox<String> cbPhong, cbTenPhim;
    private JButton btnThem, btnSua, btnXoa, btnXoaRong, btnTim, btnLamMoi;
    private DefaultTableModel model;
    private JTree treeNgayChieu;
    protected QuanLyPhim_DAO dataPhim;
    private QuanLyRap_DAO dataRap;

    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);
    private final Color darkBg = new Color(34, 34, 34);
    private final Color headerRed = new Color(175, 25, 25);

    public QuanLySuatChieu() {
        // Use GridBagLayout for the main container
        setLayout(new GridBagLayout());
        setBackground(darkBg);
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.BOTH;

        // --- 1. TITLE (Top) ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; // Spans across left and right column
        gbc.weightx = 1.0; gbc.weighty = 0.0;
        JLabel lblTitle = new JLabel("QUẢN LÝ SUẤT CHIẾU");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(orangeColor);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, gbc);

        // --- 2. LEFT SIDE (Main Content) ---
        gbc.gridwidth = 1;
        gbc.weightx = 0.8;

        // Panel Search & Buttons
        gbc.gridy = 1;
        JPanel pnTop = createRoundPanel(panelDarkTone);
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        txtTimSuat = createStyledTextField(180,35 );
        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        pnTop.add(txtTimSuat);
        pnTop.add(btnTim);
        pnTop.add(btnThem);
        pnTop.add(btnSua);
        pnTop.add(btnXoa);
        pnTop.add(btnXoaRong);
        pnTop.add(btnLamMoi);
        add(pnTop, gbc);

        // Panel Input Form
        gbc.gridy = 2;
        JPanel pnInput = createRoundPanel(panelDarkTone);
        pnInput.setLayout(new GridBagLayout());
        setupInputForm(pnInput);
        add(pnInput, gbc);

        // Table
        gbc.gridy = 3;
        gbc.weighty = 1.0; // Pushes everything else up and fills bottom space
        setupTable();
        JScrollPane scrollTable = new JScrollPane(tblSuatChieu);
        scrollTable.getViewport().setBackground(tableBackground);
        scrollTable.setBorder(null);
        add(scrollTable, gbc);

        // --- 3. RIGHT SIDE (Tree Filter) ---
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridheight = 3; // Spans search, input, and table height
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;

        JPanel pnRight = createRoundPanel(panelDarkTone);
        pnRight.setLayout(new BorderLayout(10, 10));
        pnRight.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel lblFilter = new JLabel("BỘ LỌC THỜI GIAN", SwingConstants.CENTER);
        lblFilter.setForeground(orangeColor);
        lblFilter.setFont(new Font("Tahoma", Font.BOLD, 16));
        pnRight.add(lblFilter, BorderLayout.NORTH);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh sách Tháng");
        for (int i = 1; i <= 12; i++) root.add(new DefaultMutableTreeNode("Tháng: " + i));
        treeNgayChieu = new JTree(new DefaultTreeModel(root));
        setupStyledTree();

        JScrollPane scrollTree = new JScrollPane(treeNgayChieu);
        scrollTree.setOpaque(false);
        scrollTree.getViewport().setOpaque(false);
        scrollTree.setBorder(null);
        pnRight.add(scrollTree, BorderLayout.CENTER);
        add(pnRight, gbc);

        // --- Listeners & Data ---
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);

        layTenPhim();
        layPhong();
        chonSuatChieu();
        capNhatBang();
        chonNut();
    }

    private void setupInputForm(JPanel p) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 20, 10, 20); // Increased spacing
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL; // This makes them stretch
        c.weightx = 1.0; // Gives them horizontal "weight" to expand

        // Initialize with a larger height (e.g., 35 or 40 pixels)
        txtMaSuat = createStyledTextField(250, 35);
        txtMaPhim = createStyledTextField(250, 35);
        txtMaPhim.setEditable(false);
        txtNgayChieu = createStyledTextField(250, 35);
        txtThoiGian = createStyledTextField(250, 35);
        txtGiaVe = createStyledTextField(250, 35);
        cbTenPhim = createStyledComboBox(250, 35);
        cbPhong = createStyledComboBox(250, 35);

        // Row 1: Mã Suất and Mã Phim
        c.gridy = 0;
        c.gridx = 0; c.weightx = 0; p.add(createLabel("Mã suất:"), c);
        c.gridx = 1; c.weightx = 1.0; p.add(txtMaSuat, c);
        c.gridx = 2; c.weightx = 0; p.add(createLabel("Mã phim:"), c);
        c.gridx = 3; c.weightx = 1.0; p.add(txtMaPhim, c);

        // Row 2: Tên Phim and Phòng
        c.gridy = 1;
        c.gridx = 0; c.weightx = 0; p.add(createLabel("Tên phim:"), c);
        c.gridx = 1; c.weightx = 1.0; p.add(cbTenPhim, c);
        c.gridx = 2; c.weightx = 0; p.add(createLabel("Phòng:"), c);
        c.gridx = 3; c.weightx = 1.0; p.add(cbPhong, c);

        // Row 3: Ngày Chiếu and Thời Gian
        c.gridy = 2;
        c.gridx = 0; c.weightx = 0; p.add(createLabel("Ngày chiếu:"), c);
        c.gridx = 1; c.weightx = 1.0; p.add(txtNgayChieu, c);
        c.gridx = 2; c.weightx = 0; p.add(createLabel("Thời gian:"), c);
        c.gridx = 3; c.weightx = 1.0; p.add(txtThoiGian, c);

        // Row 4: Giá Vé
        c.gridy = 3;
        c.gridx = 0; c.weightx = 0; p.add(createLabel("Giá vé:"), c);
        c.gridx = 1; c.weightx = 1.0; p.add(txtGiaVe, c);
    }
    private void setupTable() {
        model = new DefaultTableModel(new String[] { "Mã Suất", "Mã Phim", "Tên Phim", "Phòng", "Ngày chiếu", "Thời gian", "Giá vé" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSuatChieu = new JTable(model);
        tblSuatChieu.setBackground(tableBackground);
        tblSuatChieu.setForeground(Color.WHITE);
        tblSuatChieu.setRowHeight(35);
        tblSuatChieu.getTableHeader().setBackground(headerRed);
        tblSuatChieu.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tblSuatChieu.getColumnCount(); i++)
            tblSuatChieu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // --- Component Styling Helpers ---
    private JPanel createRoundPanel(Color color) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Tahoma", Font.BOLD, 14));
        l.setForeground(Color.WHITE);
        return l;
    }

    private JTextField createStyledTextField(int width, int height) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(width, height));
        tf.setBackground(orangeColor);
        tf.setForeground(Color.BLACK);
        tf.setBorder(new EmptyBorder(0, 10, 0, 10));
        tf.setFont(new Font("Tahoma", Font.BOLD, 16)); // Slightly larger font
        tf.setCaretColor(Color.BLACK);
        return tf;
    }

    private JComboBox<String> createStyledComboBox(int width, int height) {
        JComboBox<String> cb = new JComboBox<>();
        cb.setPreferredSize(new Dimension(width, height));
        cb.setBackground(orangeColor);
        cb.setForeground(Color.BLACK);
        cb.setFont(new Font("Tahoma", Font.BOLD, 14));
        // To remove the default border on some systems
        cb.setBorder(BorderFactory.createLineBorder(orangeColor));
        return cb;
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
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    private void setupStyledTree() {
        treeNgayChieu.setOpaque(false);
        treeNgayChieu.setRowHeight(35);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(panelDarkTone);
        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setBackgroundSelectionColor(orangeColor);
        renderer.setTextSelectionColor(Color.BLACK);
        renderer.setLeafIcon(null); renderer.setClosedIcon(null); renderer.setOpenIcon(null);
        treeNgayChieu.setCellRenderer(renderer);
    }

    // ================= Logic (DAO Interactions) ====================
    @Override
    public void loadData() {
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        layTenPhim();
        capNhatBang();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) them();
        else if (src == btnSua) sua();
        else if (src == btnXoa) xoa();
        else if (src == btnXoaRong) xoaRong();
        else if (src == btnTim) tim();
        else if (src == btnLamMoi) { loadData(); xoaRong(); }
    }

    private String tenPhim(String maPhim) {
        dataPhim = new QuanLyPhim_DAO();
        Phim phim = dataPhim.timPhimTheoMa(maPhim);
        return (phim != null) ? phim.getTenPhim() : "N/A";
    }

    private void layTenPhim() {
        dataPhim = new QuanLyPhim_DAO();
        ArrayList<Phim> ds = dataPhim.getAllPhim();
        cbTenPhim.removeAllItems();
        cbTenPhim.addItem("---:---");
        if (ds != null) for (Phim p : ds) cbTenPhim.addItem(p.getTenPhim());
        cbTenPhim.addActionListener(e -> {
            String selected = (String) cbTenPhim.getSelectedItem();
            if (selected == null || selected.equals("---:---")) { txtMaPhim.setText(""); return; }
            for (Phim p : ds) if (p.getTenPhim().equals(selected)) { txtMaPhim.setText(p.getMaPhim()); break; }
        });
    }

    private void layPhong() {
        dataRap = new QuanLyRap_DAO();
        ArrayList<Rap> ds = dataRap.getAllRap();
        cbPhong.removeAllItems();
        cbPhong.addItem("---:---");
        if (ds != null) for (Rap r : ds) cbPhong.addItem(r.getMaRap() + " - " + r.getTenRap());
    }

    private void capNhatBang() {
        model.setRowCount(0);
        for (SuatChieu s : quanLySuatChieu_DAO.getAllSuatChieu()) {
            model.addRow(new Object[] {
                    s.getMaSuatChieu(), s.getMaPhim(), tenPhim(s.getMaPhim()), s.getMaRap(),
                    s.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    s.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                    s.getGiaVe()
            });
        }
    }

    private void chonSuatChieu() {
        tblSuatChieu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = tblSuatChieu.getSelectedRow();
            if (row < 0) return;
            txtMaSuat.setText(model.getValueAt(row, 0).toString());
            txtMaPhim.setText(model.getValueAt(row, 1).toString());
            cbTenPhim.setSelectedItem(model.getValueAt(row, 2).toString());
            txtNgayChieu.setText(model.getValueAt(row, 4).toString());
            txtThoiGian.setText(model.getValueAt(row, 5).toString());
            txtGiaVe.setText(model.getValueAt(row, 6).toString());
            String maRap = model.getValueAt(row, 3).toString();
            for (int i = 0; i < cbPhong.getItemCount(); i++)
                if (cbPhong.getItemAt(i).startsWith(maRap)) { cbPhong.setSelectedIndex(i); break; }
        });
    }

    private void chonNut() {
        treeNgayChieu.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNgayChieu.getLastSelectedPathComponent();
            if (node == null || node.isRoot()) return;
            String month = node.toString().replace("Tháng:", "").trim();
            capNhatBangTheoThang(month);
        });
    }

    private void capNhatBangTheoThang(String m) {
        model.setRowCount(0);
        int mInt = Integer.parseInt(m);
        for (SuatChieu s : quanLySuatChieu_DAO.getAllSuatChieu()) {
            if (s.getNgayChieu().getMonthValue() == mInt) {
                model.addRow(new Object[] {
                        s.getMaSuatChieu(), s.getMaPhim(), tenPhim(s.getMaPhim()), s.getMaRap(),
                        s.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        s.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                        s.getGiaVe()
                });
            }
        }
    }

    private void tim() {
        String ma = txtTimSuat.getText().trim();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                tblSuatChieu.setRowSelectionInterval(i, i);
                tblSuatChieu.scrollRectToVisible(tblSuatChieu.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy!");
    }

    private void xoaRong() {
        txtMaSuat.setText(""); txtMaPhim.setText(""); txtNgayChieu.setText("");
        txtThoiGian.setText(""); txtGiaVe.setText("");
        cbTenPhim.setSelectedIndex(0); cbPhong.setSelectedIndex(0);
        tblSuatChieu.clearSelection();
    }

    private void xoa() {
        int row = tblSuatChieu.getSelectedRow();
        if (row < 0) return;
        String ma = txtMaSuat.getText();
        if(JOptionPane.showConfirmDialog(this, "Xóa " + ma + "?", "Xác nhận", 0) == 0) {
            quanLySuatChieu_DAO.removeSuatChieu(quanLySuatChieu_DAO.timSuatChieu(ma));
            capNhatBang(); xoaRong();
        }
    }

    private void sua() {
        SuatChieu sc = validateData();
        if (sc != null && quanLySuatChieu_DAO.updateSuatChieu(sc)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            capNhatBang();
        }
    }

    private void them() {
        SuatChieu sc = validateData();
        if (sc != null && quanLySuatChieu_DAO.addNewSuatChieu(sc)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            capNhatBang();
        }
    }

    private SuatChieu validateData() {
        try {
            String maS = txtMaSuat.getText().trim();
            String maP = txtMaPhim.getText().trim();
            String phongStr = (String)cbPhong.getSelectedItem();
            String ngayS = txtNgayChieu.getText().trim();
            String gioS = txtThoiGian.getText().trim();
            String giaS = txtGiaVe.getText().trim();
            if (maS.isEmpty() || maP.isEmpty() || phongStr.equals("---:---")) return null;
            LocalDate ngay = LocalDate.parse(ngayS, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime gio = LocalTime.parse(gioS, DateTimeFormatter.ofPattern("HH:mm"));
            float gia = Float.parseFloat(giaS);
            String maR = phongStr.split("-")[0].trim();
            return new SuatChieu(maS, maP, maR, ngay, gio, gia);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập sai định dạng!");
            return null;
        }
    }
}