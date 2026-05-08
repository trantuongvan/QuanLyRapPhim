package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import dao.QuanLyHoaDon_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entity.LoadData;
import entity.Phim;
import entity.SuatChieu;

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

    @Override
    public void loadData() {
        capNhatBang();
    }

    public QuanLyThongKe() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);
        Color orangeColor = new Color(245, 140, 0);
        Color redColor = new Color(175, 25, 25);

        quanLyPhim_Dao = new QuanLyPhim_DAO();
        quanLyHoaDon_DAO = new QuanLyHoaDon_DAO();
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        JLabel lblTitle = new JLabel("Báo cáo thống kê");
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

        txtTimKiem = new JTextField() {
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
        txtTimKiem.setOpaque(false);
        txtTimKiem.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimKiem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtTimKiem.setForeground(Color.BLACK);
        txtTimKiem.setCaretColor(Color.BLACK);
        txtTimKiem.setPreferredSize(new Dimension(150, 35));
        txtTimKiem.setMinimumSize(new Dimension(150, 35));

        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnBaoCao = taoNutBoGoc("Lập Báo cáo", orangeColor);
        btnBaoCao.setPreferredSize(new Dimension(140, 35));
        btnBaoCao.setMaximumSize(new Dimension(140, 35));
        btnXem = taoNutBoGoc("Làm mới", orangeColor);

        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.insets = new Insets(0, 5, 0, 5);
        gbcTop.gridy = 0;

        gbcTop.gridx = 0;
        gbcTop.weightx = 1.0;
        pnTop.add(txtTimKiem, gbcTop);
        gbcTop.weightx = 0.0;
        gbcTop.gridx = 1; pnTop.add(btnTim, gbcTop);
        gbcTop.gridx = 2; pnTop.add(btnBaoCao, gbcTop);
        gbcTop.gridx = 3; pnTop.add(btnXem, gbcTop);

        pnTopAndForm.add(pnTop, BorderLayout.NORTH);

        //input
        JPanel pnInput = new JPanel(new BorderLayout(0, 10)) {
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

        JLabel lblThongTin = new JLabel("THỐNG KÊ TỔNG QUAN", SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 18));
        pnInput.add(lblThongTin, BorderLayout.NORTH);

        JPanel pnStatsLayout = new JPanel(new GridLayout(2, 3, 15, 5));
        pnStatsLayout.setOpaque(false);

        Font fontLbl = new Font("Tahoma", Font.BOLD, 15);
        JLabel lblTotalPhimTitle = new JLabel("Tổng số phim", SwingConstants.CENTER);
        lblTotalPhimTitle.setFont(fontLbl);
        JLabel lblTotalVeTitle = new JLabel("Tổng số vé đã bán", SwingConstants.CENTER);
        lblTotalVeTitle.setFont(fontLbl);
        JLabel lblTotalDoanhThuTitle = new JLabel("Tổng doanh thu (vnđ)", SwingConstants.CENTER);
        lblTotalDoanhThuTitle.setFont(fontLbl);

        JTextField[] tfs = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            tfs[i] = new JTextField("0");
            tfs[i].setOpaque(true);
            tfs[i].setBackground(Color.WHITE);
            tfs[i].setBorder(null);
            tfs[i].setForeground(Color.BLACK);
            tfs[i].setFont(new Font("Tahoma", Font.BOLD, 18));
            tfs[i].setHorizontalAlignment(JTextField.CENTER);
            tfs[i].setEditable(false);
        }
        txtTotalPhim = tfs[0];
        txtTotalVe = tfs[1];
        txtTotalDoanhThu = tfs[2];
        txtTotalDoanhThu.setForeground(redColor);

        pnStatsLayout.add(lblTotalPhimTitle);
        pnStatsLayout.add(lblTotalVeTitle);
        pnStatsLayout.add(lblTotalDoanhThuTitle);
        pnStatsLayout.add(txtTotalPhim);
        pnStatsLayout.add(txtTotalVe);
        pnStatsLayout.add(txtTotalDoanhThu);

        pnInput.add(pnStatsLayout, BorderLayout.CENTER);
        pnTopAndForm.add(pnInput, BorderLayout.CENTER);

        //table
        String[] columns = { "Mã phim", "Tên phim", "Ngày", "Số vé đã bán", "Tổng doanh thu (vnđ)" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblThongKe = new JTable(model);
        tblThongKe.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblThongKe.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        tblThongKe.setRowHeight(35);
        tblThongKe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblThongKe.getTableHeader().setBackground(redColor);
        tblThongKe.getTableHeader().setForeground(Color.WHITE);
        ((DefaultTableCellRenderer)tblThongKe.getTableHeader().getDefaultRenderer()).setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tblThongKe.getColumnCount(); i++){
            tblThongKe.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        scrollTable.getViewport().setBackground(Color.WHITE);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        pnCenter.add(scrollTable, BorderLayout.CENTER);

        //ds tháng
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

        DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
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

        btnXem.addActionListener(this);
        btnBaoCao.addActionListener(this);
        btnTim.addActionListener(this);

        capNhatBang();
        chonNut();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnXem) {
            capNhatBang();
            txtTimKiem.setText("");
        } else if (src == btnTim) {
            timKiem();
        } else if (src == btnBaoCao) {
            if (month != null) {
                lapBaoCao(month);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tháng để lập báo cáo!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
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

    private void tinhTongThongKeTheoThang(String thang) {
        int totalDoanhThu = 0;
        int totalPhim = 0;
        int totalVe = 0;

        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            if (ktPhimTrongThang(phim.getMaPhim(), Integer.parseInt(thang))) {
                totalPhim++;
                int soLuongVe = quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
                totalVe += soLuongVe;
                totalDoanhThu += quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
            }
        }
        txtTotalPhim.setText(String.valueOf(totalPhim));
        txtTotalVe.setText(String.valueOf(totalVe));
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        txtTotalDoanhThu.setText(nf.format(totalDoanhThu));
    }

    private boolean ktPhimTrongThang(String maPhim, int month) {
        if (quanLySuatChieu_DAO == null)
            quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            if (suat.getMaPhim().equalsIgnoreCase(maPhim) && suat.getNgayChieu().getMonthValue() == month) {
                return true;
            }
        }
        return false;
    }

    private void chonNut() {
        treeNgayChieu.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNgayChieu.getLastSelectedPathComponent();
            if (node == null) return;
            String text = node.toString();
            if (text.startsWith("Tháng:")) {
                try {
                    month = text.replace("Tháng:", "").trim();
                    capNhatBangTheoThang(month);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Định dạng tháng không hợp lệ!");
                }
            } else {
                capNhatBang();
            }
        });
    }

    private void tinhTongThongKe() {
        int totalPhim = model.getRowCount();
        int totalVe = 0;
        long totalDoanhThu = 0L;
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            totalVe += quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
            totalDoanhThu += quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
        }
        txtTotalPhim.setText(String.valueOf(totalPhim));
        txtTotalVe.setText(String.valueOf(totalVe));
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        txtTotalDoanhThu.setText(nf.format(totalDoanhThu));
    }

    private void capNhatBang() {
        model.setRowCount(0);
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            model.addRow(new Object[] {
                    phim.getMaPhim(),
                    phim.getTenPhim(),
                    ngayChieu(phim.getMaPhim()),
                    quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim()),
                    quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim())
            });
        }
        tinhTongThongKe();
    }

    private String ngayChieu(String maPhim) {
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            if (suat.getMaPhim().equalsIgnoreCase(maPhim)) {
                return suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }
        return null;
    }

    private void capNhatBangTheoThang(String monthStr) {
        model.setRowCount(0);
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            String dateStr = ngayChieu(phim.getMaPhim());
            if (dateStr == null || dateStr.trim().isEmpty()) {
                continue;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (date.getMonthValue() == Integer.parseInt(monthStr)) {
                    model.addRow(new Object[] {
                            phim.getMaPhim(),
                            phim.getTenPhim(),
                            dateStr,
                            quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim()),
                            quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim())
                    });
                }
            } catch (Exception ex) {
                System.err.println("Bỏ qua phim do lỗi parse ngày: " + phim.getMaPhim() + " -> " + dateStr);
            }
        }
        tinhTongThongKeTheoThang(monthStr);
    }

    private void timKiem() {
        String maTim = txtTimKiem.getText().trim();
        if (maTim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phim cần tìm!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(maTim)) {
                tblThongKe.setRowSelectionInterval(i, i);
                tblThongKe.scrollRectToVisible(tblThongKe.getCellRect(i, 0, true));
                JOptionPane.showMessageDialog(this, "Đã tìm thấy phim!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy phim!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void lapBaoCao(String month) {
        new BaoCaoUI(month);
    }
}