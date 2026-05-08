package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;

import dao.QuanLyHoaDon_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import entity.LoadData;
import entity.Phim;
import entity.SuatChieu;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class QuanLyThongKe extends JPanel implements ActionListener, LoadData {
    private JTable tblThongKe;
    private JButton btnXem, btnBaoCao, btnTim;
    private JTextField txtTotalPhim, txtTotalVe, txtTotalDoanhThu, txtTimKiem;
    private JTree treeNgayChieu;
    private DefaultTableModel model;

    private QuanLySuatChieu_DAO quanLySuatChieu_DAO;
    private QuanLyPhim_DAO quanLyPhim_Dao;
    private QuanLyHoaDon_DAO quanLyHoaDon_DAO;
    private String month;

    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);
    private final Color darkBg = new Color(34, 34, 34);
    private final Color headerRed = new Color(175, 25, 25);

    public QuanLyThongKe() {
        quanLyPhim_Dao = new QuanLyPhim_DAO();
        quanLyHoaDon_DAO = new QuanLyHoaDon_DAO();
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        setLayout(new GridBagLayout());
        setBackground(darkBg);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.05;
        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(orangeColor);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 0.05;
        JPanel pnTop = createRoundPanel(panelDarkTone);
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel lblTim = new JLabel("Mã phim:");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 14));

        txtTimKiem = createStyledTextField(250);
        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnBaoCao = taoNutBoGoc("Lập Báo cáo", orangeColor);
        btnBaoCao.setPreferredSize(new Dimension(140, 35));
        btnBaoCao.setMaximumSize(new Dimension(140, 35));
        btnXem = taoNutBoGoc("Làm mới", orangeColor);

        pnTop.add(lblTim);
        pnTop.add(txtTimKiem);
        pnTop.add(btnTim);
        pnTop.add(btnBaoCao);
        pnTop.add(btnXem);
        add(pnTop, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.1;
        JPanel pnSummary = createRoundPanel(panelDarkTone);
        pnSummary.setLayout(new GridLayout(1, 3, 20, 0));
        pnSummary.setBorder(new EmptyBorder(5, 20, 5, 20));

        txtTotalPhim = createSummaryField();
        txtTotalVe = createSummaryField();
        txtTotalDoanhThu = createSummaryField();
        txtTotalDoanhThu.setForeground(new Color(255, 80, 80));

        pnSummary.add(createSummaryBox("TỔNG PHIM", txtTotalPhim));
        pnSummary.add(createSummaryBox("TỔNG VÉ", txtTotalVe));
        pnSummary.add(createSummaryBox("DOANH THU", txtTotalDoanhThu));
        add(pnSummary, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.8;
        setupTable();
        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(tableBackground);
        add(scrollTable, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0.2;
        JPanel pnRight = createRoundPanel(panelDarkTone);
        pnRight.setLayout(new BorderLayout(10, 10));
        pnRight.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel lblFilter = new JLabel("BỘ LỌC THỜI GIAN", SwingConstants.CENTER);
        lblFilter.setForeground(orangeColor);
        lblFilter.setFont(new Font("Tahoma", Font.BOLD, 16));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh sách Tháng");
        for (int i = 1; i <= 12; i++) root.add(new DefaultMutableTreeNode("Tháng: " + i));
        treeNgayChieu = new JTree(new DefaultTreeModel(root));
        setupStyledTree();

        JScrollPane scrollTree = new JScrollPane(treeNgayChieu);
        scrollTree.setOpaque(false);
        scrollTree.getViewport().setOpaque(false);
        scrollTree.setBorder(null);

        pnRight.add(lblFilter, BorderLayout.NORTH);
        pnRight.add(scrollTree, BorderLayout.CENTER);
        add(pnRight, gbc);

        btnXem.addActionListener(this);
        btnBaoCao.addActionListener(this);
        btnTim.addActionListener(this);
        chonNut();
        loadData();
    }

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

    private JTextField createStyledTextField(int width) {
        JTextField tf = new JTextField();
        tf.setBackground(orangeColor);
        tf.setForeground(Color.BLACK);
        tf.setBorder(new EmptyBorder(0, 10, 0, 10));
        tf.setPreferredSize(new Dimension(width, 30));
        tf.setFont(new Font("Tahoma", Font.BOLD, 14));
        return tf;
    }

    private JPanel createSummaryBox(String title, JTextField field) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField createSummaryField() {
        JTextField tf = new JTextField("0");
        tf.setEditable(false);
        tf.setOpaque(false);
        tf.setBorder(null);
        tf.setForeground(Color.WHITE);
        tf.setFont(new Font("Tahoma", Font.BOLD, 22));
        tf.setHorizontalAlignment(JTextField.CENTER);
        return tf;
    }

    private void setupTable() {
        String[] columns = { "Mã phim", "Tên phim", "Ngày chiếu", "Số vé đã bán", "Doanh thu (VNĐ)" };
        model = new DefaultTableModel(columns, 0);
        tblThongKe = new JTable(model);
        tblThongKe.setBackground(tableBackground);
        tblThongKe.setForeground(Color.WHITE);
        tblThongKe.setRowHeight(35);
        tblThongKe.getTableHeader().setBackground(headerRed);
        tblThongKe.getTableHeader().setForeground(Color.WHITE);
        tblThongKe.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tblThongKe.getColumnCount(); i++){
            tblThongKe.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setupStyledTree() {
        treeNgayChieu.setOpaque(false);
        treeNgayChieu.setRowHeight(30);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(panelDarkTone);
        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setBackgroundSelectionColor(orangeColor);
        renderer.setTextSelectionColor(Color.BLACK);
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        treeNgayChieu.setCellRenderer(renderer);
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
    public void loadData() { capNhatBang(); }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnXem) { capNhatBang(); txtTimKiem.setText(""); }
        else if (src == btnTim) { timKiem(); }
        else if (src == btnBaoCao) {
            if (month != null) new BaoCaoUI(month);
            else JOptionPane.showMessageDialog(this, "Chọn tháng trước!");
        }
    }

    private void capNhatBang() {
        model.setRowCount(0);
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            model.addRow(new Object[] {
                    phim.getMaPhim(), phim.getTenPhim(), ngayChieu(phim.getMaPhim()),
                    quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim()),
                    formatCurrency(quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim()))
            });
        }
        tinhTongThongKe();
    }

    private String ngayChieu(String maPhim) {
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            if (suat.getMaPhim().equalsIgnoreCase(maPhim))
                return suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "N/A";
    }

    private String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount);
    }

    private void tinhTongThongKe() {
        int totalVe = 0; long totalDoanhThu = 0;
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            totalVe += quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
            totalDoanhThu += quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
        }
        txtTotalPhim.setText(String.valueOf(model.getRowCount()));
        txtTotalVe.setText(String.valueOf(totalVe));
        txtTotalDoanhThu.setText(formatCurrency(totalDoanhThu));
    }

    private void chonNut() {
        treeNgayChieu.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNgayChieu.getLastSelectedPathComponent();
            if (node == null || node.isRoot()) return;
            month = node.toString().replace("Tháng:", "").trim();
            capNhatBangTheoThang(month);
        });
    }

    private void capNhatBangTheoThang(String m) {
        model.setRowCount(0);
        int totalV = 0; long totalD = 0;
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            String dateStr = ngayChieu(phim.getMaPhim());
            if (dateStr.equals("N/A")) continue;
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (date.getMonthValue() == Integer.parseInt(m)) {
                int v = quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
                double d = quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
                model.addRow(new Object[] { phim.getMaPhim(), phim.getTenPhim(), dateStr, v, formatCurrency(d) });
                totalV += v; totalD += d;
            }
        }
        txtTotalPhim.setText(String.valueOf(model.getRowCount()));
        txtTotalVe.setText(String.valueOf(totalV));
        txtTotalDoanhThu.setText(formatCurrency(totalD));
    }

    private void timKiem() {
        String maTim = txtTimKiem.getText().trim();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(maTim)) {
                tblThongKe.setRowSelectionInterval(i, i);
                tblThongKe.scrollRectToVisible(tblThongKe.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy mã phim này!");
    }
}