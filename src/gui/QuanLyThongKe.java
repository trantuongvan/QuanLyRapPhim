package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
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
        setLayout(null);

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);
        Color orangeColor = new Color(245, 140, 0);

        quanLyPhim_Dao = new QuanLyPhim_DAO();
        quanLyHoaDon_DAO = new QuanLyHoaDon_DAO();
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(orangeColor);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(20, 15, 1210, 40);
        add(lblTitle);

        JPanel pnTop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        pnTop.setOpaque(false);
        pnTop.setLayout(null);
        pnTop.setBounds(20, 70, 940, 60);
        add(pnTop);

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
        txtTimKiem.setBounds(178, 15, 200, 30);
        pnTop.add(txtTimKiem);

        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnBaoCao = taoNutBoGoc("Lập Báo cáo", orangeColor);
        btnXem = taoNutBoGoc("Làm mới", orangeColor);
        btnTim.setBounds(393, 15, 80, 30);
        pnTop.add(btnTim);
        btnBaoCao.setBounds(488, 15, 140, 30);
        pnTop.add(btnBaoCao);
        btnXem.setBounds(643, 15, 120, 30);
        pnTop.add(btnXem);

        JPanel pnInput = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        pnInput.setOpaque(false);
        pnInput.setLayout(null);
        pnInput.setBounds(20, 150, 940, 240);
        add(pnInput);

        JLabel lblThongTin = new JLabel("THỐNG KÊ TỔNG QUAN");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblThongTin.setBounds(0, 10, 940, 30);
        pnInput.add(lblThongTin);

        JTextField[] tfs = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            tfs[i] = new JTextField("0") {
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
            tfs[i].setBorder(new EmptyBorder(0, 15, 0, 15));
            tfs[i].setForeground(Color.BLACK);
            tfs[i].setFont(new Font("Tahoma", Font.BOLD, 18));
            tfs[i].setHorizontalAlignment(JTextField.CENTER);
            tfs[i].setEditable(false);
        }

        txtTotalPhim = tfs[0];
        txtTotalVe = tfs[1];
        txtTotalDoanhThu = tfs[2];

        Font fontLbl = new Font("Tahoma", Font.BOLD, 16);
        int y1 = 70, y2 = 140;
        int wLbl = 200, hComp = 35, wFld = 240;

        JLabel lblTotalPhimTitle = new JLabel("Tổng số phim:");
        lblTotalPhimTitle.setFont(fontLbl);
        lblTotalPhimTitle.setBounds(60, y1, wLbl, hComp);
        pnInput.add(lblTotalPhimTitle);
        txtTotalPhim.setBounds(250, y1, wFld, hComp);
        pnInput.add(txtTotalPhim);

        JLabel lblTotalVeTitle = new JLabel("Tổng số vé đã bán:");
        lblTotalVeTitle.setFont(fontLbl);
        lblTotalVeTitle.setBounds(530, y1, 180, hComp);
        pnInput.add(lblTotalVeTitle);
        txtTotalVe.setBounds(710, y1, 190, hComp);
        pnInput.add(txtTotalVe);

        JLabel lblTotalDoanhThuTitle = new JLabel("Tổng doanh thu (vnđ):");
        lblTotalDoanhThuTitle.setFont(fontLbl);
        lblTotalDoanhThuTitle.setBounds(60, y2, wLbl, hComp);
        pnInput.add(lblTotalDoanhThuTitle);
        txtTotalDoanhThu.setBounds(250, y2, wFld, hComp);
        txtTotalDoanhThu.setForeground(new Color(178, 34, 34));
        pnInput.add(txtTotalDoanhThu);




        String[] columns = { "Mã phim", "Tên phim", "Ngày", "Số vé đã bán", "Tổng doanh thu (vnđ)" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblThongKe = new JTable(model);
        tblThongKe.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tblThongKe.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        tblThongKe.setRowHeight(35);
        tblThongKe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tblThongKe.getColumnCount(); i++){
            tblThongKe.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        scrollTable.setBounds(20, 420, 940, 360);
        scrollTable.getViewport().setBackground(Color.WHITE);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        add(scrollTable);

        JPanel pnRight = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnRight.setLayout(null);
        pnRight.setOpaque(false);
        pnRight.setBounds(1000, 70, 270, 710);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh sách Tháng chiếu");
        String[] thangList = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
        for (String thang : thangList) {
            root.add(new DefaultMutableTreeNode("Tháng: " + thang));
        }

        treeNgayChieu = new JTree(new DefaultTreeModel(root));
        treeNgayChieu.setFont(new Font("Tahoma", Font.PLAIN, 16));
        treeNgayChieu.setBorder(new EmptyBorder(10, 10, 10, 10));

        //render lại list tháng
        treeNgayChieu.setRowHeight(35);
        treeNgayChieu.putClientProperty("JTree.lineStyle", "Angled");
        javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
        renderer.setBackgroundSelectionColor(orangeColor);
        renderer.setTextSelectionColor(Color.WHITE);
        renderer.setBackgroundNonSelectionColor(Color.WHITE);
        renderer.setTextNonSelectionColor(Color.BLACK);
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        renderer.setBorderSelectionColor(null);
        treeNgayChieu.setCellRenderer(renderer);

        JScrollPane scrollTree = new JScrollPane(treeNgayChieu);
        scrollTree.setBounds(10 , 10, 230, 690);
        scrollTree.setBorder(BorderFactory.createEmptyBorder());
        pnRight.add(scrollTree);

        add(pnRight);

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
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setBackground(bgColor);
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