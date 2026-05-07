package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import dao.QuanLyKhachHang_DAO;
import entity.KhachHang;
import entity.LoadData;
import entity.TheLoaiPhim;

public class QuanLyKhachHang extends JPanel implements ActionListener, MouseListener, LoadData {

    private QuanLyKhachHang_DAO kh_dao;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtMaKH, txtHoTen, txtSoDT, txtDiaChi, txtTimKiem;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnXoaTrang, btnXoa1Dong, btnLamMoi, btnSua, btnTimKiem;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 16);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 16);
    private final Color orangeColor = new Color(245, 140, 0);

    @Override
    public void loadData() {
        DocDuLieuVaoTable();
        xoaTrang();
    }

    public QuanLyKhachHang() {
        kh_dao = new QuanLyKhachHang_DAO();

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(new Color(34, 34, 34));

        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
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

        txtTimKiem = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(orangeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        txtTimKiem.setOpaque(false);
        txtTimKiem.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimKiem.setFont(FONT_TXT);
        txtTimKiem.setForeground(Color.BLACK);
        txtTimKiem.setCaretColor(Color.BLACK);
        txtTimKiem.setMaximumSize(new Dimension(500, 35));

        btnTimKiem = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa1Dong = taoNutBoGoc("Xóa", orangeColor);
        btnXoaTrang = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        pnTop.add(txtTimKiem);
        pnTop.add(Box.createHorizontalGlue());
        pnTop.add(btnTimKiem); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnThem); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnSua); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoa1Dong); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnXoaTrang); pnTop.add(Box.createHorizontalStrut(10));
        pnTop.add(btnLamMoi);

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

        JLabel lblThongTin = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblThongTin.setForeground(Color.BLACK);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        pnInput.add(lblThongTin, gbc);

        JTextField[] tfs = new JTextField[4];
        for (int i = 0; i < 4; i++) {
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

        txtMaKH = tfs[0]; txtMaKH.setEditable(false);
        txtHoTen = tfs[1]; txtSoDT = tfs[2]; txtDiaChi = tfs[3];

        cboGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
        cboGioiTinh.setBackground(orangeColor);
        cboGioiTinh.setForeground(Color.BLACK);
        cboGioiTinh.setFont(FONT_TXT);
        cboGioiTinh.setPreferredSize(new Dimension(200, 35));

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Mã khách hàng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtMaKH, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        pnInput.add(taoLabel("Họ tên:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
        pnInput.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(cboGioiTinh, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        pnInput.add(taoLabel("Số điện thoại:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
        pnInput.add(txtSoDT, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        pnInput.add(taoLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; gbc.gridwidth = 3;
        pnInput.add(txtDiaChi, gbc);

        pnCenterTop.add(pnInput);
        add(pnCenterTop, BorderLayout.NORTH);

        String[] headers = "Mã KH;Họ tên;Giới tính;Số ĐT;Địa chỉ".split(";");
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(scroll, BorderLayout.CENTER);

        // SỰ KIỆN
        table.addMouseListener(this);
        btnThem.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnXoa1Dong.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSua.addActionListener(this);
        btnTimKiem.addActionListener(this);

        DocDuLieuVaoTable();
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


    private void DocDuLieuVaoTable() {
        tableModel.setRowCount(0);
        List<KhachHang> ds = kh_dao.getDanhSachKhachHang();
        if (ds == null) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối hoặc không có dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (KhachHang kh : ds) {
            tableModel.addRow(new Object[] {
                    kh.getMaKH(), kh.getHoTen(), kh.getGioiTinh(),
                    kh.getSoDT(), kh.getDiaChi()
            });
        }
    }

    private void xoaTrang() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSoDT.setText("");
        txtDiaChi.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtTimKiem.setText("");
        table.clearSelection();
        txtHoTen.requestFocus();
    }

    private boolean validateInput() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSoDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String maKH_hien_tai = txtMaKH.getText().trim();

        if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin khách hàng", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!hoTen.matches("^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\\s[A-ZÀ-Ỹ][a-zà-ỹ]+)+$")) {
            JOptionPane.showMessageDialog(this, "Gồm phần họ và tên, chữ cái đầu phải viết hoa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (!sdt.matches("^(03|05|07|08|09)[0-9]{8}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 số và 2 số đầu phải là 03, 05, 07,...", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDT.requestFocus();
            return false;
        }

        if (!diaChi.matches("^[A-Ỹa-ỹ0-9/,\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được có kí tự đặc biệt.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        KhachHang kh_trung_sdt = kh_dao.timKhachHangTheoSDT(sdt);

        if (kh_trung_sdt != null) {
            if (maKH_hien_tai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số điện thoại này đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoDT.requestFocus();
                return false;
            } else if (!maKH_hien_tai.equals(kh_trung_sdt.getMaKH())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại này đã tồn tại cho một khách hàng khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoDT.requestFocus();
                return false;
            }
        }
        return true;
    }

    private KhachHang layKhachHangTuForm() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSoDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String gioiTinh = cboGioiTinh.getSelectedItem().toString();

        String maKH = txtMaKH.getText().trim();
        if(maKH.isEmpty()) {
            maKH = QuanLyKhachHang_DAO.taoMaKHTuDong();
        }
        return new KhachHang(maKH, hoTen, gioiTinh, sdt, diaChi);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src.equals(btnLamMoi)) {
            DocDuLieuVaoTable();
            xoaTrang();
            return;
        }

        if (src.equals(btnXoaTrang)) {
            xoaTrang();
            return;
        }

        if (src.equals(btnThem)) {
            if (!txtMaKH.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Đang ở chế độ sửa. Nhấn 'Xóa trắng' để chuyển sang chế độ thêm mới.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (validateInput()) {
                KhachHang kh = layKhachHangTuForm();
                if (kh == null) return;

                if (kh_dao.add(kh)) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    DocDuLieuVaoTable();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        if (src.equals(btnSua)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Bạn phải chọn một dòng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtMaKH.setText(tableModel.getValueAt(row, 0).toString());

            if (validateInput()) {
                KhachHang kh = layKhachHangTuForm();
                if (kh == null) return;

                if (kh_dao.update(kh)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    DocDuLieuVaoTable();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        if (src.equals(btnXoa1Dong)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Bạn phải chọn một dòng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maKH = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa khách hàng " + maKH + "?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (kh_dao.delete(maKH)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        DocDuLieuVaoTable();
                        xoaTrang();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa khách hàng này!\nKhách hàng đã có lịch sử giao dịch (hóa đơn).",
                            "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        if (src.equals(btnTimKiem)) {
            String maTim = txtTimKiem.getText().trim();
            if (maTim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng cần tìm!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            DocDuLieuVaoTable();
            xoaTrang();

            int index = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(maTim)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                table.setRowSelectionInterval(index, index);
                table.scrollRectToVisible(table.getCellRect(index, 0, true));
                mouseClicked(null);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với mã " + maTim, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaKH.setText(tableModel.getValueAt(row, 0).toString());
            txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
            cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 2).toString());
            txtSoDT.setText(tableModel.getValueAt(row, 3).toString());
            txtDiaChi.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}