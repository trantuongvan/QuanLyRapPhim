package gui;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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

    private ArrayList<HoaDon> danhSach = new ArrayList<>();

    public QuanLyHoaDon() {
        setLayout(null);

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);

        // ======= KẾT NỐI DATABASE =======
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            if (con != null)
                System.out.println("Kết nối SQL Server thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }

        billManager = new QuanLyHoaDon_DAO();

        Color orangeColor = new Color(245, 140, 0);

        JLabel lblTieuDe = new JLabel("Quản lý hóa đơn");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 15, 1300, 40);
        add(lblTieuDe);

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
        pnTop.setLayout(null);
        pnTop.setBounds(40, 70, 1220, 60);
        add(pnTop);

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
        txtTimHD.setBounds(140, 15, 450, 30);
        pnTop.add(txtTimHD);

        btnTim = taoNutBoGoc("Xem", new Color(160, 82, 45));
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLuu = taoNutBoGoc("Lưu", orangeColor);

        btnTim.setBounds(620, 15, 80, 30);
        pnTop.add(btnTim);
        btnXoa.setBounds(720, 15, 90, 30);
        pnTop.add(btnXoa);
        btnXoaRong.setBounds(830, 15, 110, 30);
        pnTop.add(btnXoaRong);
        btnLuu.setBounds(960, 15, 90, 30);
        pnTop.add(btnLuu);

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
        pnInput.setLayout(null);
        pnInput.setBounds(40, 150, 1220, 200);
        add(pnInput);

        JLabel lblThongTin = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblThongTin.setForeground(Color.BLACK);
        lblThongTin.setBounds(0, 10, 1220, 30);
        pnInput.add(lblThongTin);

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
        }

        txtMaKH = tfs[0];
        txtMaKH.setEditable(false);
        txtMaHD = tfs[1];
        txtMaHD.setEditable(false);
        txtMaNV = tfs[2];
        txtMaNV.setEditable(false);
        txtNgayLap = tfs[3];
        txtNgayLap.setEditable(false);
        txtSoLuongVe = tfs[4];
        txtSoLuongVe.setEditable(false);
        txtTongTien = tfs[5];
        txtTongTien.setEditable(false);

        int y1 = 45, y2 = 95, y3 = 145;
        int wLbl = 130, hComp = 35, wFld = 350;

        JLabel lblMaPhim = new JLabel("Mã hóa đơn:");
        lblMaPhim.setFont(FONT_LBL);
        lblMaPhim.setBounds(80, y1, wLbl, hComp);
        pnInput.add(lblMaPhim);
        txtMaHD.setBounds(210, y1, wFld, hComp);
        pnInput.add(txtMaHD);

        JLabel lblNhaSX = new JLabel("Mã nhân viên:");
        lblNhaSX.setFont(FONT_LBL);
        lblNhaSX.setBounds(80, y2, wLbl, hComp);
        pnInput.add(lblNhaSX);
        txtMaNV.setBounds(210, y2, wFld, hComp);
        pnInput.add(txtMaNV);

        JLabel lblThoiLuong = new JLabel("Mã khách hàng:");
        lblThoiLuong.setFont(FONT_LBL);
        lblThoiLuong.setBounds(80, y3, wLbl, hComp);
        pnInput.add(lblThoiLuong);
        txtMaKH.setBounds(210, y3, wFld, hComp);
        pnInput.add(txtMaKH);

        JLabel lblTenPhim = new JLabel("Ngày lập:");
        lblTenPhim.setFont(FONT_LBL);
        lblTenPhim.setBounds(650, y1, wLbl, hComp);
        pnInput.add(lblTenPhim);
        txtNgayLap.setBounds(780, y1, wFld, hComp);
        pnInput.add(txtNgayLap);

        JLabel lblTheLoai = new JLabel("Số lượng vé:");
        lblTheLoai.setFont(FONT_LBL);
        lblTheLoai.setBounds(650, y2, wLbl, hComp);
        pnInput.add(lblTheLoai);
        txtSoLuongVe.setBounds(780, y2, wFld, hComp);
        pnInput.add(txtSoLuongVe);

        JLabel lblQuocGia = new JLabel("Tổng tiền:");
        lblQuocGia.setFont(FONT_LBL);
        lblQuocGia.setBounds(650, y3, wLbl, hComp);
        pnInput.add(lblQuocGia);
        txtTongTien.setBounds(780, y3, wFld, hComp);
        pnInput.add(txtTongTien);

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
        scroll.setBounds(40, 380, 1220, 400);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        //  SỰ KIỆN
        loadData();

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> xoaRong());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());
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
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(130, 50));
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

    private void setFormEditable(boolean editable) {
        this.txtMaHD.setEditable(editable);
        this.txtNgayLap.setEditable(editable);
        this.txtMaNV.setEditable(editable);
        this.txtMaKH.setEditable(editable);
        this.txtSoLuongVe.setEditable(editable);
        this.txtTongTien.setEditable(editable);
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
