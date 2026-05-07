package gui;

import javax.swing.*;
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
    private JLabel lblTotalPhimValue, lblTotalVeValue, lblTotalDoanhThuValue;
    private JTextField txtTimKiem;
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
        quanLyPhim_Dao = new QuanLyPhim_DAO();
        quanLyHoaDon_DAO = new QuanLyHoaDon_DAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ===== NORTH =====
        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(new Color(220, 20, 60));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== WEST (JTree ngày chiếu) =====
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh sách Tháng chiếu");
        String[] thangList = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        };
        for (String thang : thangList) {
            root.add(new DefaultMutableTreeNode("Tháng: " + thang));
        }

        treeNgayChieu = new JTree(new DefaultTreeModel(root));
        JScrollPane scrollTree = new JScrollPane(treeNgayChieu);
        scrollTree.setPreferredSize(new Dimension(220, 0));
        add(scrollTree, BorderLayout.WEST);

        // ===== CENTER =====
        JPanel pnCenter = new JPanel(new BorderLayout(10, 10));
        pnCenter.setBackground(Color.WHITE);
        add(pnCenter, BorderLayout.CENTER);

        // === Thống kê tổng quan ===
        JPanel pnThongKe = new JPanel(new GridLayout(1, 4, 12, 5));
        pnThongKe.setBackground(Color.WHITE);
        pnThongKe.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));

        Font fTitle = new Font("Tahoma", Font.BOLD, 15);
        Font fValue = new Font("Tahoma", Font.BOLD, 20);
        // -- Tổng phim --
        JPanel pnTotalPhim = new JPanel(new BorderLayout());
        pnTotalPhim.setBackground(Color.WHITE);
        JLabel lblTotalPhimTitle = new JLabel("Tổng số phim", SwingConstants.CENTER);
        lblTotalPhimTitle.setFont(fTitle);
        lblTotalPhimTitle.setForeground(Color.BLACK);
        lblTotalPhimValue = new JLabel("0", SwingConstants.CENTER);
        lblTotalPhimValue.setFont(fValue);
        lblTotalPhimValue.setForeground(Color.BLACK);
        pnTotalPhim.add(lblTotalPhimTitle, BorderLayout.NORTH);
        pnTotalPhim.add(lblTotalPhimValue, BorderLayout.CENTER);
        pnThongKe.add(pnTotalPhim);
        // -- Tổng vé --
        JPanel pnTotalVe = new JPanel(new BorderLayout());
        pnTotalVe.setBackground(Color.WHITE);
        JLabel lblTotalVeTitle = new JLabel("Tổng số vé đã bán", SwingConstants.CENTER);
        lblTotalVeTitle.setFont(fTitle);
        lblTotalVeTitle.setForeground(Color.BLACK);
        lblTotalVeValue = new JLabel("0", SwingConstants.CENTER);
        lblTotalVeValue.setFont(fValue);
        lblTotalVeValue.setForeground(Color.BLACK);
        pnTotalVe.add(lblTotalVeTitle, BorderLayout.NORTH);
        pnTotalVe.add(lblTotalVeValue, BorderLayout.CENTER);
        pnThongKe.add(pnTotalVe);
        // -- Tổng doanh thu --
        JPanel pnTotalDoanhThu = new JPanel(new BorderLayout());
        pnTotalDoanhThu.setBackground(Color.WHITE);
        JLabel lblTotalDoanhThuTitle = new JLabel("Tổng doanh thu (vnđ)", SwingConstants.CENTER);
        lblTotalDoanhThuTitle.setFont(fTitle);
        lblTotalDoanhThuTitle.setForeground(Color.BLACK);
        lblTotalDoanhThuValue = new JLabel("0", SwingConstants.CENTER);
        lblTotalDoanhThuValue.setFont(fValue);
        lblTotalDoanhThuValue.setForeground(Color.BLACK);
        pnTotalDoanhThu.add(lblTotalDoanhThuTitle, BorderLayout.NORTH);
        pnTotalDoanhThu.add(lblTotalDoanhThuValue, BorderLayout.CENTER);
        pnThongKe.add(pnTotalDoanhThu);

        pnCenter.add(pnThongKe, BorderLayout.NORTH);

        // === BẢNG DỮ LIỆU ===
        String[] columns = { "Mã phim", "Tên phim", "Ngày", "Số vé đã bán", "Tổng doanh thu (vnđ)" };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblThongKe = new JTable(model);
        tblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tblThongKe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tblThongKe.setRowHeight(28);
        JScrollPane scrollTable = new JScrollPane(tblThongKe);
        pnCenter.add(scrollTable, BorderLayout.CENTER);

        tinhTongThongKe();

        // ===== SOUTH (nút chức năng) =====
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnSouth.setBackground(Color.WHITE);

        JLabel lblTim = new JLabel("Tìm mã phim:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnSouth.add(lblTim);
        pnSouth.add(txtTimKiem);
        btnTim = new JButton("Tìm");
        btnBaoCao = new JButton("Lập Báo cáo");
        btnXem = new JButton("Làm mới");

        JButton[] arrBtns = { btnTim, btnBaoCao, btnXem };
        Color[] colors = {
                new Color(231, 76, 60),
                new Color(46, 204, 113),
                new Color(52, 152, 219)

        };

        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);
        for (int i = 0; i < arrBtns.length; i++) {
            arrBtns[i].setFont(btnFont);
            arrBtns[i].setBackground(colors[i]);
            arrBtns[i].setForeground(Color.WHITE);
            arrBtns[i].setFocusPainted(false);
            arrBtns[i].setPreferredSize(new Dimension(160, 45));
            arrBtns[i].addActionListener(this);
            pnSouth.add(arrBtns[i]);
        }

        add(pnSouth, BorderLayout.SOUTH);
        capNhatBang();
        chonNut();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnXem)
            capNhatBang();
        else if (src == btnTim)
            timKiem();
        else if (src == btnBaoCao)
            if (month != null) {
                lapBaoCao(month);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tháng để lập báo cáo!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }

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
        lblTotalPhimValue.setText(String.valueOf(totalPhim));
        lblTotalVeValue.setText(String.valueOf(totalVe));
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        lblTotalDoanhThuValue.setText(nf.format(totalDoanhThu));
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
        // ===== Sự kiện chọn node trên cây =====
        treeNgayChieu.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNgayChieu.getLastSelectedPathComponent();
            if (node == null)
                return;
            String text = node.toString();
            if (text.startsWith("Tháng:")) {
                try {
                    // chỉ có tháng và không có ngày chiếu nên lấy tháng
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
        // compute totals from DAOs to avoid parsing formatted strings
        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            totalVe += quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
            totalDoanhThu += quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
        }
        lblTotalPhimValue.setText(String.valueOf(totalPhim));
        lblTotalVeValue.setText(String.valueOf(totalVe));
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        lblTotalDoanhThuValue.setText(nf.format(totalDoanhThu));
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
                // không có suất chiếu cho phim này -> bỏ qua
                continue;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (date.getMonthValue() == Integer.parseInt(monthStr)) {
                    // Chú ý: cột đã khai báo là {"Mã phim", "Ngày", "Tên phim", ...}
                    model.addRow(new Object[] {
                            phim.getMaPhim(),
                            dateStr,
                            phim.getTenPhim(),
                            quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim()),
                            quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim())
                    });
                }
            } catch (Exception ex) {
                // Nếu parse thất bại, bỏ qua phim này (tránh ném ngoại lệ làm vỡ giao diện)
                System.err.println("Bỏ qua phim do lỗi parse ngày: " + phim.getMaPhim() + " -> " + dateStr);
            }
        }

        tinhTongThongKeTheoThang(monthStr);

    }

    private void timKiem() {
        String maTim = txtTimKiem.getText().trim();
        if (maTim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất cần tìm!", "Thông báo",
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
        JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void lapBaoCao(String month) {
        new BaoCaoUI(month);

    }

}