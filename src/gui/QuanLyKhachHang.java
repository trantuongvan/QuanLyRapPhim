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

    @Override
    public void loadData() {
        DocDuLieuVaoTable();
        xoaTrang();
    }

    public QuanLyKhachHang() {
        setLayout(null);

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);

        kh_dao = new QuanLyKhachHang_DAO();

        Color orangeColor = new Color(245, 140, 0);

        JLabel lblTieuDe = new JLabel("Quản lý khách hàng");
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
        txtTimKiem.setBounds(20, 15, 450, 30);
        pnTop.add(txtTimKiem);

        btnTimKiem = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa1Dong = taoNutBoGoc("Xóa", orangeColor);
        btnXoaTrang = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        btnTimKiem.setBounds(500, 15, 80, 30);
        pnTop.add(btnTimKiem);
        btnThem.setBounds(600, 15, 90, 30);
        pnTop.add(btnThem);
        btnSua.setBounds(710, 15, 90, 30);
        pnTop.add(btnSua);
        btnXoa1Dong.setBounds(820, 15, 90, 30);
        pnTop.add(btnXoa1Dong);
        btnXoaTrang.setBounds(930, 15, 110, 30);
        pnTop.add(btnXoaTrang);
        btnLamMoi.setBounds(1060, 15, 110, 30);
        pnTop.add(btnLamMoi);

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

        JLabel lblThongTin = new JLabel("THÔNG TIN KHÁCH HÀNG");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblThongTin.setForeground(Color.BLACK);
        lblThongTin.setBounds(0, 10, 1220, 30);
        pnInput.add(lblThongTin);

        JTextField[] tfs = new JTextField[5];
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
        }

        txtMaKH = tfs[0];
        txtMaKH.setEditable(false);
        txtHoTen = tfs[1];
        txtSoDT = tfs[2];
        txtDiaChi = tfs[3];

        cboGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
        cboGioiTinh.setBackground(orangeColor);
        cboGioiTinh.setForeground(Color.BLACK);
        cboGioiTinh.setBorder(null);
        cboGioiTinh.setFont(FONT_TXT);

        int y1 = 45, y2 = 95, y3 = 145;
        int wLbl = 130, hComp = 35, wFld = 350;

        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setFont(FONT_LBL);
        lblMaKH.setBounds(80, y1, wLbl, hComp);
        pnInput.add(lblMaKH);
        txtMaKH.setBounds(210, y1, wFld, hComp);
        pnInput.add(txtMaKH);

        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(FONT_LBL);
        lblGioiTinh.setBounds(80, y2, wLbl, hComp);
        pnInput.add(lblGioiTinh);
        cboGioiTinh.setBounds(210, y2, wFld, hComp);
        pnInput.add(cboGioiTinh);

        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(FONT_LBL);
        lblDiaChi.setBounds(80, y3, wLbl, hComp);
        pnInput.add(lblDiaChi);
        txtDiaChi.setBounds(210, y3, 920, hComp);
        pnInput.add(txtDiaChi);

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(FONT_LBL);
        lblHoTen.setBounds(650, y1, wLbl, hComp);
        pnInput.add(lblHoTen);
        txtHoTen.setBounds(780, y1, wFld, hComp);
        pnInput.add(txtHoTen);

        JLabel lblSdt = new JLabel("Số điện thoại:");
        lblSdt.setFont(FONT_LBL);
        lblSdt.setBounds(650, y2, wLbl, hComp);
        pnInput.add(lblSdt);
        txtSoDT.setBounds(780, y2, wFld, hComp);
        pnInput.add(txtSoDT);


        //------------------------

//        JLabel lblMaKH = new JLabel("Mã khách hàng:");
//        lblMaKH.setBounds(40, 40, 120, 30);
//        lblMaKH.setFont(FONT_LBL);
//        pnInput.add(lblMaKH);
//
//        txtMaKH = new JTextField();
//        txtMaKH.setBounds(170, 40, 200, 30);
//        txtMaKH.setEditable(false);
//        styleTextField(txtMaKH);
//        pnInput.add(txtMaKH);
//
//        JLabel lblHoTen = new JLabel("Họ tên:");
//        lblHoTen.setBounds(410, 40, 120, 30);
//        lblHoTen.setFont(FONT_LBL);
//        pnInput.add(lblHoTen);
//
//        txtHoTen = new JTextField();
//        txtHoTen.setBounds(540, 40, 200, 30);
//        styleTextField(txtHoTen);
//        pnInput.add(txtHoTen);
//
//        JLabel lblGioiTinh = new JLabel("Giới tính:");
//        lblGioiTinh.setBounds(40, 100, 120, 30);
//        lblGioiTinh.setFont(FONT_LBL);
//        pnInput.add(lblGioiTinh);

//        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
//        cboGioiTinh.setBounds(170, 100, 200, 30);
//        cboGioiTinh.setFont(FONT_TXT);
//        pnInput.add(cboGioiTinh);
//
//        JLabel lblSoDT = new JLabel("Số điện thoại:");
//        lblSoDT.setBounds(410, 100, 120, 30);
//        lblSoDT.setFont(FONT_LBL);
//        pnInput.add(lblSoDT);
//
//        txtSoDT = new JTextField();
//        txtSoDT.setBounds(540, 100, 200, 30);
//        styleTextField(txtSoDT);
//        pnInput.add(txtSoDT);
//
//        JLabel lblDiaChi = new JLabel("Địa chỉ:");
//        lblDiaChi.setBounds(40, 160, 120, 30);
//        lblDiaChi.setFont(FONT_LBL);
//        pnInput.add(lblDiaChi);
//
//        txtDiaChi = new JTextField();
//        txtDiaChi.setBounds(170, 160, 570, 30);
//        styleTextField(txtDiaChi);
//        pnInput.add(txtDiaChi);

//        JPanel pnActions = new JPanel();
//        pnActions.setLayout(null);
//        pnActions.setBackground(Color.WHITE);
//        pnActions.setBounds(840, 60, 320, 240);
//        add(pnActions);
//
//        JLabel lblTim = new JLabel("Nhập mã KH cần tìm:");
//        lblTim.setFont(FONT_LBL);
//        lblTim.setBounds(20, 10, 180, 30);
//        pnActions.add(lblTim);
//
//        txtTimKiem = new JTextField();
//        txtTimKiem.setBounds(20, 45, 180, 30);
//        styleTextField(txtTimKiem);
//        pnActions.add(txtTimKiem);
//
//        btnTimKiem = new JButton("Tìm");
//        btnTimKiem.setIcon(new ImageIcon("icon/search.png"));
//        styleButton(btnTimKiem, new Color(108, 117, 125));
//        btnTimKiem.setBounds(210, 45, 90, 30);
//        pnActions.add(btnTimKiem);
//
//        btnThem = new JButton("Thêm");
//        btnThem.setIcon(new ImageIcon("icon/add.png"));
//        styleButton(btnThem, new Color(0, 123, 255));
//        btnThem.setBounds(20, 90, 135, 45);
//        pnActions.add(btnThem);
//
//        btnSua = new JButton("Sửa");
//        btnSua.setIcon(new ImageIcon("icon/edit.png"));
//        styleButton(btnSua, new Color(23, 162, 184));
//        btnSua.setBounds(165, 90, 135, 45);
//        pnActions.add(btnSua);
//
//        btnXoa1Dong = new JButton("Xóa");
//        btnXoa1Dong.setIcon(new ImageIcon("icon/delete.png"));
//        styleButton(btnXoa1Dong, new Color(220, 53, 69));
//        btnXoa1Dong.setBounds(20, 145, 135, 45);
//        pnActions.add(btnXoa1Dong);
//
//        btnXoaTrang = new JButton("Xóa rỗng");
//        btnXoaTrang.setIcon(new ImageIcon("icon/clear.png"));
//        styleButton(btnXoaTrang, new Color(255, 193, 7));
//        btnXoaTrang.setBounds(165, 145, 135, 45);
//        pnActions.add(btnXoaTrang);
//
//        btnLamMoi = new JButton("Làm mới Table");
//        btnLamMoi.setIcon(new ImageIcon("icon/refresh.png"));
//        styleButton(btnLamMoi, new Color(108, 117, 125));
//        btnLamMoi.setBounds(20, 200, 280, 35);
//        pnActions.add(btnLamMoi);

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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
 
        scroll.setBounds(40, 380, 1220, 400);
        add(scroll);

        table.addMouseListener(this);
        btnThem.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnXoa1Dong.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSua.addActionListener(this);
        btnTimKiem.addActionListener(this);

//        btnSua.setEnabled(false);
//        btnXoa1Dong.setEnabled(false);
        DocDuLieuVaoTable();
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

//        btnSua.setEnabled(false);
//        btnXoa1Dong.setEnabled(false);
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