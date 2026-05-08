package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ConnectDB.ConnectDB;
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
import java.time.format.DateTimeParseException;
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

    @Override
    public void loadData() {
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        layTenPhim();
        capNhatBang();
    }

    public QuanLySuatChieu() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);
        Color orangeColor = new Color(245, 140, 0);
        Color redColor = new Color(175, 25, 25);

        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        JLabel lblTitle = new JLabel("Quản lý suất chiếu");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(orangeColor);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnCenter = new JPanel(new BorderLayout(0, 15));
        pnCenter.setOpaque(false);
        add(pnCenter, BorderLayout.CENTER);

        JPanel pnTopAndForm = new JPanel(new BorderLayout(0, 15));
        pnTopAndForm.setOpaque(false);
        pnCenter.add(pnTopAndForm, BorderLayout.NORTH);

        //CRUD
        JPanel pnTop = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnTop.setOpaque(false);
        pnTop.setBorder(new EmptyBorder(12, 15, 12, 15));

        txtTimSuat = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(orangeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        txtTimSuat.setOpaque(false);
        txtTimSuat.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimSuat.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtTimSuat.setForeground(Color.BLACK);
        txtTimSuat.setCaretColor(Color.BLACK);
        txtTimSuat.setPreferredSize(new Dimension(200, 35));
        txtTimSuat.setMinimumSize(new Dimension(200, 35));

        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.insets = new Insets(0, 5, 0, 5);
        gbcTop.gridy = 0;
        gbcTop.gridx = 0;
        gbcTop.weightx = 1.0;
        pnTop.add(txtTimSuat, gbcTop);
        gbcTop.weightx = 0.0;

        gbcTop.gridx = 1;
        pnTop.add(btnTim, gbcTop);
        gbcTop.gridx = 2;
        pnTop.add(btnThem, gbcTop);
        gbcTop.gridx = 3;
        pnTop.add(btnSua, gbcTop);
        gbcTop.gridx = 4;
        pnTop.add(btnXoa, gbcTop);
        gbcTop.gridx = 5;
        pnTop.add(btnXoaRong, gbcTop);
        gbcTop.gridx = 6;
        pnTop.add(btnLamMoi, gbcTop);

        pnTopAndForm.add(pnTop, BorderLayout.NORTH);

        //Input
        JPanel pnInput = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnInput.setOpaque(false);
        pnInput.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel lblThongTin = new JLabel("THÔNG TIN SUẤT CHIẾU");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 18));
        pnInput.add(lblThongTin, BorderLayout.NORTH);

        JPanel pnForm = new JPanel(new GridBagLayout());
        pnForm.setOpaque(false);

        JTextField[] tfs = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            tfs[i] = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(orangeColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tfs[i].setOpaque(false);
            tfs[i].setBorder(new EmptyBorder(0, 10, 0, 10));
            tfs[i].setForeground(Color.BLACK);
            tfs[i].setFont(new Font("Tahoma", Font.PLAIN, 16));
            tfs[i].setCaretColor(Color.BLACK);
            tfs[i].setPreferredSize(new Dimension(0, 35));
        }

        txtMaSuat = tfs[0];
        txtMaPhim = tfs[1]; txtMaPhim.setEditable(false);
        txtNgayChieu = tfs[2];
        txtThoiGian = tfs[3];
        txtGiaVe = tfs[4];

        cbTenPhim = new JComboBox<>();
        layTenPhim();
        cbTenPhim.setBackground(orangeColor);
        cbTenPhim.setForeground(Color.BLACK);
        cbTenPhim.setBorder(null);
        cbTenPhim.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cbTenPhim.setPreferredSize(new Dimension(0, 35));

        cbPhong = new JComboBox<>();
        layPhong();
        cbPhong.setBackground(orangeColor);
        cbPhong.setForeground(Color.BLACK);
        cbPhong.setBorder(null);
        cbPhong.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cbPhong.setPreferredSize(new Dimension(0, 35));

        Font fontLbl = new Font("Tahoma", Font.BOLD, 16);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Mã suất:", fontLbl), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtMaSuat, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Mã phim:", fontLbl), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnForm.add(txtMaPhim, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Tên phim:", fontLbl), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(cbTenPhim, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Phòng chiếu:", fontLbl), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnForm.add(cbPhong, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Ngày chiếu:", fontLbl), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtNgayChieu, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Thời gian:", fontLbl), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnForm.add(txtThoiGian, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Giá vé:", fontLbl), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtGiaVe, gbc);

        pnInput.add(pnForm, BorderLayout.CENTER);
        pnTopAndForm.add(pnInput, BorderLayout.CENTER);

        //table
        model = new DefaultTableModel(new String[] { "Mã Suất", "Mã Phim", "Tên Phim", "Phòng", "Ngày chiếu", "Thời gian", "Giá vé" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSuatChieu = new JTable(model);
        tblSuatChieu.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblSuatChieu.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        tblSuatChieu.setRowHeight(35);
        tblSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSuatChieu.getTableHeader().setBackground(redColor);
        tblSuatChieu.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tblSuatChieu.getColumnCount(); i++){
            tblSuatChieu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTable = new JScrollPane(tblSuatChieu);
        scrollTable.getViewport().setBackground(Color.WHITE);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        pnCenter.add(scrollTable, BorderLayout.CENTER);

        JPanel pnRight = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnRight.setOpaque(false);
        pnRight.setBorder(new EmptyBorder(15, 10, 15, 10));
        pnRight.setPreferredSize(new Dimension(250, 0));
        add(pnRight, BorderLayout.EAST);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh sách Tháng chiếu");
        String[] thangList = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
        for (String thang : thangList) {
            root.add(new DefaultMutableTreeNode("Tháng: " + thang));
        }

        treeNgayChieu = new JTree(new DefaultTreeModel(root));
        treeNgayChieu.setFont(new Font("Tahoma", Font.PLAIN, 16));
        treeNgayChieu.setRowHeight(35);
        treeNgayChieu.putClientProperty("JTree.lineStyle", "Angled");

        javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
        renderer.setBackgroundSelectionColor(orangeColor);
        renderer.setTextSelectionColor(Color.WHITE);
        renderer.setBackgroundNonSelectionColor(Color.WHITE);
        renderer.setTextNonSelectionColor(Color.BLACK);
        renderer.setLeafIcon(null); renderer.setClosedIcon(null); renderer.setOpenIcon(null);
        renderer.setBorderSelectionColor(null);
        treeNgayChieu.setCellRenderer(renderer);

        JScrollPane scrollTree = new JScrollPane(treeNgayChieu);
        scrollTree.setBorder(BorderFactory.createEmptyBorder());
        pnRight.add(scrollTree, BorderLayout.CENTER);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);

        chonSuatChieu();
        capNhatBang();
        chonNut();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            them();
        } else if (src == btnSua) {
            sua();
        } else if (src == btnXoa) {
            xoa();
        } else if (src == btnXoaRong) {
            xoaRong();
        } else if (src == btnTim) {
            tim();
        }
        else if (src.equals(btnLamMoi)) {
            loadData();
            xoaRong();
        }
    }


    private void chonNut() {
        // ===== Sự kiện chọn node trên cây =====
        treeNgayChieu.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNgayChieu.getLastSelectedPathComponent();
            if (node == null)
                return;
            String text = node.toString();
            if (text.startsWith("Tháng:")) {
                try {
                    // chỉ có tháng và không có ngày chiếu nên lấy tháng
                    String month = text.replace("Tháng:", "").trim();
                    capNhatBangTheoThang(month);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Định dạng tháng không hợp lệ!");
                }
            } else {
                capNhatBang();
            }
        });
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
    private JLabel taoLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }
    private String tenPhim(String maPhim) {
        dataPhim = new QuanLyPhim_DAO();
        Phim phim = dataPhim.timPhimTheoMa(maPhim);
        // return dataPhim.timPhimTheoMa(maPhim).getTenPhim();
        if (phim != null) {
            return phim.getTenPhim();
        }
        return null;
    }

    private void chonSuatChieu() {
        tblSuatChieu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return; // tránh xử lý 2 lần
            int row = tblSuatChieu.getSelectedRow();
            if (row < 0)
                return;
            try {
                String maSuat = String.valueOf(model.getValueAt(row, 0));
                String maPhim = String.valueOf(model.getValueAt(row, 1));
                String tenPhim = model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "";
                String maRap = model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "";
                String ngay = model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "";
                String gio = model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "";
                String gia = model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "";

                txtMaSuat.setText(maSuat);
                txtMaPhim.setText(maPhim);
                // chọn tên phim trong combobox (nếu tồn tại)
                if (tenPhim != null && !tenPhim.isEmpty()) {
                    cbTenPhim.setSelectedItem(tenPhim);
                }
                txtNgayChieu.setText(ngay);
                txtThoiGian.setText(gio);
                txtGiaVe.setText(gia);

                // Chọn phòng tương ứng trong cbPhong. cbPhong chứa các mục dạng "ma - ten"
                boolean found = false;
                for (int i = 0; i < cbPhong.getItemCount(); i++) {
                    String item = cbPhong.getItemAt(i);
                    if (item != null) {
                        // kiểm tra bắt đầu bằng mã
                        if (item.startsWith(maRap + " -") || item.startsWith(maRap + "-")
                                || item.startsWith(maRap + " ")) {
                            cbPhong.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                        // hoặc chứa mã ở đầu nếu có khoảng trắng khác
                        if (item.startsWith(maRap)) {
                            cbPhong.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    // nếu không tìm thấy, giữ nguyên hoặc chọn mục đầu
                    if (cbPhong.getItemCount() > 0)
                        cbPhong.setSelectedIndex(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void layTenPhim() {
        try {
            dataPhim = new QuanLyPhim_DAO();
            ArrayList<Phim> danhSach = dataPhim.getAllPhim();
            cbTenPhim.removeAllItems();
            // thêm mục placeholder ở vị trí đầu
            String placeholder = "---:---";
            cbTenPhim.addItem(placeholder);

            if (danhSach == null || danhSach.isEmpty()) {
                // nếu chưa có phim, thêm thông báo nhưng giữ placeholder ở đầu
                cbTenPhim.addItem("(Chưa có phim)");
                cbTenPhim.setSelectedIndex(0);
                return;
            }
            for (Phim p : danhSach) {
                cbTenPhim.addItem(p.getTenPhim());
            }
            // đặt mặc định chọn placeholder
            cbTenPhim.setSelectedIndex(0);
            // khi chọn 1 phim thì tự động điền mã phim
            cbTenPhim.addActionListener(e -> {
                String selectedTenPhim = (String) cbTenPhim.getSelectedItem();
                if (selectedTenPhim == null || selectedTenPhim.equals(placeholder)
                        || selectedTenPhim.equals("(Chưa có phim)") || selectedTenPhim.equals("(Lỗi tải phim)")) {
                    txtMaPhim.setText("");
                    return;
                }
                for (Phim p : danhSach) {
                    if (p.getTenPhim().equals(selectedTenPhim)) {
                        txtMaPhim.setText(p.getMaPhim());
                        break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cbTenPhim.removeAllItems();
            cbTenPhim.addItem("(Lỗi tải phim)");
        }
    }

    private void capNhatBang() {
        model.setRowCount(0);
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            model.addRow(new Object[] {
                    suat.getMaSuatChieu(),
                    suat.getMaPhim(),
                    tenPhim(suat.getMaPhim()),
                    suat.getMaRap(),
                    suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    suat.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                    suat.getGiaVe()
            });
        }
    }

    private void capNhatBangTheoThang(String monthStr) {
        model.setRowCount(0);
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            // lọc theo tháng
            if (suat.getNgayChieu().getMonthValue() == Integer.parseInt(monthStr)) {
                model.addRow(new Object[] {
                        suat.getMaSuatChieu(),
                        suat.getMaPhim(),
                        tenPhim(suat.getMaPhim()),
                        suat.getMaRap(),
                        suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        suat.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                        suat.getGiaVe()
                });
            }
        }
    }

    // Load danh sách phòng (rạp) từ database vào combobox
    private void layPhong() {

        try {
            dataRap = new QuanLyRap_DAO();
            ArrayList<Rap> danhSach = dataRap.getAllRap();
            cbPhong.removeAllItems();
            // thêm mục placeholder ở vị trí đầu
            String placeholder = "---:---";
            cbPhong.addItem(placeholder);

            if (danhSach == null || danhSach.isEmpty()) {
                // nếu chưa có phòng, thêm thông báo nhưng giữ placeholder ở đầu
                cbPhong.addItem("(Chưa có phòng)");
                cbPhong.setSelectedIndex(0);
                return;
            }
            for (Rap r : danhSach) {
                cbPhong.addItem(r.getMaRap() + " - " + r.getTenRap());
            }
            // đặt mặc định chọn placeholder
            cbPhong.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
            cbPhong.removeAllItems();
            cbPhong.addItem("(Lỗi tải phòng)");
        }
    }

    private void tim() {
        String maSuatTim = txtTimSuat.getText().trim();
        if (maSuatTim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất cần tìm.");
            return;
        }
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSuat = String.valueOf(model.getValueAt(i, 0));
            if (maSuat.equalsIgnoreCase(maSuatTim)) {
                tblSuatChieu.setRowSelectionInterval(i, i);
                tblSuatChieu.scrollRectToVisible(new Rectangle(tblSuatChieu.getCellRect(i, 0, true)));
                found = true;
                break;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu với mã: " + maSuatTim);
        }
    }

    private void xoaRong() {

        txtMaSuat.setText("");
        txtMaPhim.setText("");
        if (cbTenPhim.getItemCount() > 0)
            cbTenPhim.setSelectedIndex(0);
        txtNgayChieu.setText("");
        txtThoiGian.setText("");
        txtGiaVe.setText("");
        if (cbPhong.getItemCount() > 0)
            cbPhong.setSelectedIndex(0);
        tblSuatChieu.clearSelection();
    }

    private void xoa() {
        int selectedRow = tblSuatChieu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu cần xóa.");
            return;
        }

        String ma = txtMaSuat.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa suất chiếu " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if(confirm == JOptionPane.YES_OPTION) {
            String maSuat = String.valueOf(model.getValueAt(selectedRow, 0));
            SuatChieu suatChieu = quanLySuatChieu_DAO.timSuatChieu(maSuat);
            quanLySuatChieu_DAO.removeSuatChieu(suatChieu);

            model.removeRow(selectedRow);
            xoaRong();
        }
    }

    private void sua() {
        String maSuat = txtMaSuat.getText().trim();
        String maPhim = txtMaPhim.getText().trim();

        // Validate required fields early
        if (maSuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất.");
            return;
        }
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim.");
            return;
        }

        Object phongObj = cbPhong.getSelectedItem();
        if (phongObj == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu.");
            return;
        }

        String phongStr = phongObj.toString();
        String ngayChieuStr = txtNgayChieu.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String giaVeStr = txtGiaVe.getText().trim();

        try {
            // UI uses dd/MM/yyyy (label shows this); parse accordingly
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime thoiGian = LocalTime.parse(thoiGianStr, DateTimeFormatter.ofPattern("HH:mm"));
            float giaVe = Float.parseFloat(giaVeStr);

            String maRap = phongStr.contains("-") ? phongStr.split("-")[0].trim() : phongStr.trim();

            SuatChieu suatChieu = new SuatChieu(maSuat, maPhim, maRap, ngayChieu, thoiGian, giaVe);

            // kiểm tra khoảng cách thời gian với các suất khác trong cùng phòng cùng ngày
            SuatChieu conflict = KiemTraSuatChieu(maRap, ngayChieu, thoiGian, maSuat);
            if (conflict != null) {
                JOptionPane.showMessageDialog(this,
                        "Xung đột giờ chiếu với suất " + conflict.getMaSuatChieu() + " lúc "
                                + conflict.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " — các suất phải cách nhau ít nhất 3 giờ.");
                return;
            }

            // Cập nhật xuống database qua DAO
            boolean updated = quanLySuatChieu_DAO.updateSuatChieu(suatChieu);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Cập nhật suất chiếu thành công.");
                // Cập nhật lại bảng từ DB
                capNhatBang();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật suất chiếu thất bại. Kiểm tra kết nối DB hoặc dữ liệu trùng.");
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng ngày/thời gian không hợp lệ. Ngày phải là dd/MM/yyyy, thời gian HH:mm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Vui lòng nhập số.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật suất chiếu: " + ex.getMessage());
        }
    }

    private void them() {
        String maSuat = txtMaSuat.getText().trim();
        String maPhim = txtMaPhim.getText().trim();

        // Validate required fields early
        if (maSuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất.");
            return;
        }
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim.");
            return;
        }

        Object phongObj = cbPhong.getSelectedItem();
        if (phongObj == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu.");
            return;
        }

        String phongStr = phongObj.toString();
        String ngayChieuStr = txtNgayChieu.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String giaVeStr = txtGiaVe.getText().trim();

        // === Biểu thức chính quy (có thể điều chỉnh theo qui tắc dự án) ===
        // Mã suất: chữ/ số/ gạch ngang/underscore, 1-20 ký tự. Thay đổi tuỳ ý.
        String reMaSuat = "^[A-Za-z0-9_-]{1,20}$";
        // Ngày dd/MM/yyyy
        String reNgay = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        // Giờ HH:mm (00-23:00-59)
        String reGio = "^([01]?\\d|2[0-3]):[0-5]\\d$";
        // Giá vé: số nguyên hoặc thập phân (tối đa 2 chữ số thập phân)
        String reGiaVe = "^\\d+(\\\\.\\d{1,2})?$";

        if (!maSuat.matches(reMaSuat)) {
            JOptionPane.showMessageDialog(this, "Mã suất không hợp lệ. Chỉ chữ, số, '-' hoặc '_' (1-20 ký tự).");
            return;
        }
        if (!ngayChieuStr.matches(reNgay)) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy.");
            return;
        }
        // giờ chiếu của các phim cùng một rap phải cách nhau 3h
        if (!thoiGianStr.matches(reGio)) {
            JOptionPane.showMessageDialog(this,
                    "Thời gian không hợp lệ. Vui lòng nhập theo định dạng HH:mm (00-23:59).");
            return;
        }
        if (!giaVeStr.matches(reGiaVe)) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Nhập số, có thể có 1-2 chữ số thập phân.");
            return;
        }

        try {
            // UI uses dd/MM/yyyy (label shows this); parse accordingly
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime thoiGian = LocalTime.parse(thoiGianStr, DateTimeFormatter.ofPattern("HH:mm"));
            float giaVe = Float.parseFloat(giaVeStr);

            String maRap = phongStr.contains("-") ? phongStr.split("-")[0].trim() : phongStr.trim();

            SuatChieu suatChieu = new SuatChieu(maSuat, maPhim, maRap, ngayChieu, thoiGian, giaVe);

            // kiểm tra khoảng cách thời gian với các suất khác trong cùng phòng cùng ngày
            SuatChieu conflict = KiemTraSuatChieu(maRap, ngayChieu, thoiGian, null);
            if (conflict != null) {
                JOptionPane.showMessageDialog(this,
                        "Xung đột giờ chiếu với suất " + conflict.getMaSuatChieu() + " lúc "
                                + conflict.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " — các suất phải cách nhau ít nhất 3 giờ.");
                return;
            }

            // Ghi xuống database qua DAO
            boolean added = quanLySuatChieu_DAO.addNewSuatChieu(suatChieu);
            if (added) {
                JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công.");
                // Cập nhật lại bảng từ DB
                capNhatBang();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm suất chiếu thất bại. Kiểm tra kết nối DB hoặc dữ liệu trùng.");
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng ngày/thời gian không hợp lệ. Ngày phải là dd/MM/yyyy, thời gian HH:mm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Vui lòng nhập số.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm suất chiếu: " + ex.getMessage());
        }
    }

    private SuatChieu KiemTraSuatChieu(String maRap, LocalDate ngay, LocalTime gio, String excludeMaSuat) {
        if (maRap == null || ngay == null || gio == null)
            return null;
        ArrayList<SuatChieu> ds = quanLySuatChieu_DAO.getAllSuatChieu();
        if (ds == null || ds.isEmpty())
            return null;
        for (int i = 0; i < ds.size(); i++) {
            SuatChieu s = ds.get(i);
            if (s == null)
                continue;
            if (excludeMaSuat != null && excludeMaSuat.equals(s.getMaSuatChieu()))
                continue;
            if (s.getMaRap() == null || !s.getMaRap().equals(maRap))
                continue;
            if (!s.getNgayChieu().isEqual(ngay))
                continue;
            long diffMinutes = Math.abs(Duration.between(s.getGioChieu(), gio).toMinutes());
            if (diffMinutes < 180)
                return s;
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản Lý Suất Chiếu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new QuanLySuatChieu());
        frame.setVisible(true);
    }
}