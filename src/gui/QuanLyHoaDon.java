package gui;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectDB;
import dao.QuanLyHoaDon_DAO;
import entity.HoaDon;
import entity.LoadData;

public class QuanLyHoaDon extends JPanel implements LoadData {
    private QuanLyHoaDon_DAO billManager = new QuanLyHoaDon_DAO();

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaHD, txtNgayLap, txtMaNV, txtMaKH, txtSoLuongVe, txtTongTien, txtTimHD;
    private JButton btnXoa, btnXoaRong, btnLuu, btnTim;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 16);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 16);
    private final Color orangeColor = new Color(245, 140, 0);

    private ArrayList<HoaDon> danhSach = new ArrayList<>();

    public QuanLyHoaDon() {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            if (con != null)
                System.out.println("Kết nối SQL Server thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }

        billManager = new QuanLyHoaDon_DAO();

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(new Color(34, 34, 34));

        JLabel lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN");
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
        pnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        txtTimHD = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(orangeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        txtTimHD.setOpaque(false);
        txtTimHD.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimHD.setFont(FONT_TXT);
        txtTimHD.setForeground(Color.BLACK);
        txtTimHD.setCaretColor(Color.BLACK);
        txtTimHD.setMaximumSize(new Dimension(500, 35));

        btnTim = taoNutBoGoc("Xem", new Color(160, 82, 45));
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLuu = taoNutBoGoc("Lưu", orangeColor);

        pnTop.add(txtTimHD);
        pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnTim); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoa); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoaRong); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnLuu);

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

        JLabel lblThongTin = new JLabel("THÔNG TIN HÓA ĐƠN");
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
            tfs[i].setEditable(false); // Tất cả đều chỉ đọc ở form này
        }

        txtMaHD = tfs[0]; txtNgayLap = tfs[1]; txtMaNV = tfs[2];
        txtSoLuongVe = tfs[3]; txtMaKH = tfs[4]; txtTongTien = tfs[5];

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Mã hóa đơn:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtMaHD, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Ngày lập:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtNgayLap, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Mã nhân viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(txtMaNV, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Số lượng vé:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(txtSoLuongVe, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        pnInput.add(taoLabel("Mã khách hàng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.5;
        pnInput.add(txtMaKH, gbc);
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        pnInput.add(taoLabel("Tổng tiền:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.5;
        pnInput.add(txtTongTien, gbc);

        pnCenterTop.add(pnInput);
        add(pnCenterTop, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Mã hóa đơn", "Ngày lập", "Mã NV", "Mã KH", "Số lượng vé", "Tổng tiền"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(scroll, BorderLayout.CENTER);

        // SỰ KIỆN
        loadData();

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> xoaRong());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());
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


    @Override
    public void loadData() {
        danhSach = billManager.getDanhSachHoaDon();
        refreshTable();
    }

    private void refreshTable() {
        this.model.setRowCount(0);
        if (danhSach == null)
            return;
        for (HoaDon hd : danhSach) {
            String ma = hd.getMaHoaDon();
            String ngay = hd.getNgayLap() != null ? hd.getNgayLap().toString() : "";
            String maNV = hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "";
            String maKH = hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "";

            int sl = this.billManager.getTongSoLuongVeCuaHoaDon(ma);
            float tt = hd.getTongTien();

            this.model.addRow(new Object[] { ma, ngay, maNV, maKH, sl, tt });
        }
    }

    private void xoaHoaDon() {
        String ma = this.txtMaHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (this.billManager.XoaHoaDonTheoMa(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cần xóa!");
            }
        }
    }

    private void timHoaDon() {
        String ma = this.txtTimHD.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn cần tìm!");
            return;
        }
        HoaDon hoaDon = this.billManager.findHoaDonByID(ma);
        if (hoaDon != null) {
            new HoaDonModal(hoaDon, this);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn có mã " + ma);
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < this.danhSach.size()) {
            HoaDon p = this.danhSach.get(i);
            this.txtMaHD.setText(p.getMaHoaDon());
            this.txtTimHD.setText(p.getMaHoaDon());
            this.txtNgayLap.setText(p.getNgayLap().toString());
            this.txtMaNV.setText(p.getNhanVien().getMaNV());
            this.txtMaKH.setText(p.getKhachHang().getMaKH());

            String soLuongVeTxt = table.getValueAt(i, 4).toString();
            this.txtSoLuongVe.setText(soLuongVeTxt);

            this.txtTongTien.setText(Float.toString(p.getTongTien()));
        }
    }

    private void xoaRong() {
        txtMaHD.setText("");
        txtNgayLap.setText("");
        txtMaNV.setText("");
        txtMaKH.setText("");
        txtSoLuongVe.setText("");
        txtTongTien.setText("");
        this.txtTimHD.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu vào CSDL!");
    }
}