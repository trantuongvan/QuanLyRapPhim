package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import dao.QuanLyPhim_DAO;
import entity.Phim;
import entity.TheLoaiPhim;
import entity.LoadData;

public class QuanLyPhim extends JPanel implements LoadData, ActionListener{
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaPhim, txtTenPhim, txtNhaSX, txtThoiLuong, txtQuocGia, txtTimPhim;
    private JComboBox<TheLoaiPhim> cboTheLoai;
    private JButton btnThem, btnSua, btnXoa, btnXoaRong, btnLamMoi, btnTim;

    private QuanLyPhim_DAO phimDAO;
    private ArrayList<Phim> dsPhim;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 16);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 16);
    private final Border BORDER_BTN = BorderFactory.createLineBorder(new Color(0, 123, 255), 1);

    public QuanLyPhim() {
        setLayout(null);

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }

        phimDAO = new QuanLyPhim_DAO();

        Color orangeColor = new Color(245, 140, 0);

        JLabel lblTieuDe = new JLabel("Quản lý phim");
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

        txtTimPhim = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(orangeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        txtTimPhim.setOpaque(false);
        txtTimPhim.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimPhim.setFont(FONT_TXT);
        txtTimPhim.setForeground(Color.BLACK);
        txtTimPhim.setCaretColor(Color.BLACK);
        txtTimPhim.setBounds(20, 15, 450, 30);
        pnTop.add(txtTimPhim);

        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        btnTim.setBounds(500, 15, 80, 30);
        pnTop.add(btnTim);
        btnThem.setBounds(600, 15, 90, 30);
        pnTop.add(btnThem);
        btnSua.setBounds(710, 15, 90, 30);
        pnTop.add(btnSua);
        btnXoa.setBounds(820, 15, 90, 30);
        pnTop.add(btnXoa);
        btnXoaRong.setBounds(930, 15, 110, 30);
        pnTop.add(btnXoaRong);
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

        JLabel lblThongTin = new JLabel("THÔNG TIN PHIM");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblThongTin.setForeground(Color.BLACK);
        lblThongTin.setBounds(0, 10, 1220, 30);
        pnInput.add(lblThongTin);

        JTextField[] tfs = new JTextField[5];
        for (int i = 0; i < 5; i++) {
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

        txtMaPhim = tfs[0]; txtMaPhim.setEditable(false);
        txtNhaSX = tfs[1];
        txtThoiLuong = tfs[2];
        txtTenPhim = tfs[3];
        txtQuocGia = tfs[4];

        cboTheLoai = new JComboBox<>(TheLoaiPhim.values());
        cboTheLoai.setBackground(orangeColor);
        cboTheLoai.setForeground(Color.BLACK);
        cboTheLoai.setBorder(null);
        cboTheLoai.setFont(FONT_TXT);

        int y1 = 45, y2 = 95, y3 = 145;
        int wLbl = 130, hComp = 35, wFld = 350;

        JLabel lblMaPhim = new JLabel("Mã phim:");
        lblMaPhim.setFont(FONT_LBL);
        lblMaPhim.setBounds(80, y1, wLbl, hComp);
        pnInput.add(lblMaPhim);
        txtMaPhim.setBounds(210, y1, wFld, hComp);
        pnInput.add(txtMaPhim);

        JLabel lblNhaSX = new JLabel("Nhà sản xuất:");
        lblNhaSX.setFont(FONT_LBL);
        lblNhaSX.setBounds(80, y2, wLbl, hComp);
        pnInput.add(lblNhaSX);
        txtNhaSX.setBounds(210, y2, wFld, hComp);
        pnInput.add(txtNhaSX);

        JLabel lblThoiLuong = new JLabel("Thời lượng:");
        lblThoiLuong.setFont(FONT_LBL);
        lblThoiLuong.setBounds(80, y3, wLbl, hComp);
        pnInput.add(lblThoiLuong);
        txtThoiLuong.setBounds(210, y3, wFld, hComp);
        pnInput.add(txtThoiLuong);

        JLabel lblTenPhim = new JLabel("Tên phim:");
        lblTenPhim.setFont(FONT_LBL);
        lblTenPhim.setBounds(650, y1, wLbl, hComp);
        pnInput.add(lblTenPhim);
        txtTenPhim.setBounds(780, y1, wFld, hComp);
        pnInput.add(txtTenPhim);

        JLabel lblTheLoai = new JLabel("Thể loại:");
        lblTheLoai.setFont(FONT_LBL);
        lblTheLoai.setBounds(650, y2, wLbl, hComp);
        pnInput.add(lblTheLoai);
        cboTheLoai.setBounds(780, y2, wFld, hComp);
        pnInput.add(cboTheLoai);

        JLabel lblQuocGia = new JLabel("Quốc gia:");
        lblQuocGia.setFont(FONT_LBL);
        lblQuocGia.setBounds(650, y3, wLbl, hComp);
        pnInput.add(lblQuocGia);
        txtQuocGia.setBounds(780, y3, wFld, hComp);
        pnInput.add(txtQuocGia);

        model = new DefaultTableModel(new String[] {
                "Mã phim", "Tên phim", "Nhà sản xuất", "Thể loại", "Thời lượng", "Quốc gia"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(40, 380, 1220, 400);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiLenForm();
            }
        });

        loadDataToTable();
        xoaRong();
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
    private void styleTextField(JTextField txt) {
        txt.setFont(FONT_TXT);
        txt.setMargin(new Insets(2, 6, 2, 6));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BORDER_BTN);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(btnThem)) themPhim();
        else if (src.equals(btnSua)) suaPhim();
        else if (src.equals(btnXoa)) xoaPhim();
        else if (src.equals(btnXoaRong)) xoaRong();
        else if (src.equals(btnTim)) timPhim();
        else if (src.equals(btnLamMoi)) {
            loadDataToTable();
            xoaRong();
        }
    }

    @Override
    public void loadData() {
        loadDataToTable();
        xoaRong();
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        dsPhim = phimDAO.getAllPhim();
        for (Phim p : dsPhim) {
            model.addRow(new Object[] {
                    p.getMaPhim(), p.getTenPhim(), p.getNhaSanXuat(),
                    p.getTheLoai().getTenHienThi(), p.getThoiLuong(), p.getQuocGia()
            });
        }
    }

    private boolean validateInput(boolean isAdding) {
        String maPhim = txtMaPhim.getText().trim();
        String tenPhim = txtTenPhim.getText().trim();
        String nhaSX = txtNhaSX.getText().trim();
        String thoiLuongStr = txtThoiLuong.getText().trim();
        String quocGia = txtQuocGia.getText().trim();

        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phim không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaPhim.requestFocus();
            return false;
        }
        if (!maPhim.matches("^P\\d{3,5}$")) { 
            JOptionPane.showMessageDialog(this, "Mã phim phải có định dạng Pxxx (ví dụ: P001, P0123).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaPhim.requestFocus();
            return false;
        }
        if (isAdding && phimDAO.timPhimTheoMa(maPhim) != null) {
            JOptionPane.showMessageDialog(this, "Mã phim đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaPhim.requestFocus();
            return false;
        }

        if (tenPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phim không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenPhim.requestFocus();
            return false;
        }
        if (!tenPhim.matches("^[A-Za-zÀ-ỹ0-9\\s'.,-]+$")) {
            JOptionPane.showMessageDialog(this, "Tên phim chỉ được chứa chữ, số, khoảng trắng và một số ký tự .,',-.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenPhim.requestFocus();
            return false;
        }

        if (nhaSX.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhà sản xuất không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNhaSX.requestFocus();
            return false;
        }
        if (!nhaSX.matches("^[A-Za-zÀ-ỹ0-9\\s'.,-]+$")) {
            JOptionPane.showMessageDialog(this, "Tên nhà sản xuất chỉ được chứa chữ, số và ký tự .,',-.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNhaSX.requestFocus();
            return false;
        }

        if (thoiLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thời lượng không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtThoiLuong.requestFocus();
            return false;
        }

        int thoiLuong;
        try {
            thoiLuong = Integer.parseInt(thoiLuongStr);
            if (thoiLuong <= 0 || thoiLuong > 500) {
                JOptionPane.showMessageDialog(this, "Thời lượng phải nằm trong khoảng 1–500 phút.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtThoiLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là một số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtThoiLuong.requestFocus();
            return false;
        }

        if (quocGia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quốc gia không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtQuocGia.requestFocus();
            return false;
        }
        if (!quocGia.matches("^[\\p{L}\\s]+$")) { 
            JOptionPane.showMessageDialog(this, "Tên quốc gia chỉ được chứa chữ và khoảng trắng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtQuocGia.requestFocus();
            return false;
        }

        return true; 
    }


    private void themPhim() {
        if (!txtMaPhim.isEditable()) {
            JOptionPane.showMessageDialog(this, "Bạn đang ở chế độ sửa. Vui lòng nhấn 'Xóa rỗng' để thêm phim mới.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validateInput(true)) { 
            return; 
        }

        try {
            Phim p = new Phim(
                    txtMaPhim.getText().trim(),
                    txtTenPhim.getText().trim(),
                    txtNhaSX.getText().trim(),
                    (TheLoaiPhim) cboTheLoai.getSelectedItem(),
                    Integer.parseInt(txtThoiLuong.getText().trim()),
                    txtQuocGia.getText().trim());

            if (phimDAO.themPhim(p)) {
                JOptionPane.showMessageDialog(this, "Thêm phim thành công!");
                loadDataToTable();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi thêm: " + e.getMessage());
        }
    }

    private void suaPhim() {
        if (txtMaPhim.isEditable() || txtMaPhim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim từ danh sách bên dưới để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (!validateInput(false)) { 
            return; 
        }
        
        try {
            Phim p = new Phim(
                    txtMaPhim.getText().trim(), 
                    txtTenPhim.getText().trim(),
                    txtNhaSX.getText().trim(),
                    (TheLoaiPhim) cboTheLoai.getSelectedItem(),
                    Integer.parseInt(txtThoiLuong.getText().trim()),
                    txtQuocGia.getText().trim());

            if (phimDAO.capNhatPhim(p)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phim cần sửa!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa phim: " + e.getMessage());
        }
    }

    private void xoaPhim() {
        if (txtMaPhim.isEditable() || txtMaPhim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim từ danh sách để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String ma = txtMaPhim.getText().trim();

        if (phimDAO.phimDaDuocSuDung(ma)) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa phim vì phim đã có suất chiếu hoặc vé liên quan!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa phim " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (phimDAO.xoaPhim(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void timPhim() {
        String ma = txtTimPhim.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã phim cần tìm!");
            return;
        }
        Phim p = phimDAO.timPhimTheoMa(ma);
        if (p != null) {
            txtMaPhim.setText(p.getMaPhim());
            txtTenPhim.setText(p.getTenPhim());
            txtNhaSX.setText(p.getNhaSanXuat());
            cboTheLoai.setSelectedItem(p.getTheLoai());
            txtThoiLuong.setText(String.valueOf(p.getThoiLuong()));
            txtQuocGia.setText(p.getQuocGia());
            txtMaPhim.setEditable(false);

            int index = -1;
            for (int i = 0; i < dsPhim.size(); i++) {
                if (dsPhim.get(i).getMaPhim().equals(p.getMaPhim())) {
                    index = i;
                    break;
                }
            }
            
            if (index != -1) {
                table.setRowSelectionInterval(index, index);
                table.scrollRectToVisible(table.getCellRect(index, 0, true));
            }

        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phim có mã " + ma);
            xoaRong(); 
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < dsPhim.size()) {
            Phim p = dsPhim.get(i);
            txtMaPhim.setText(p.getMaPhim());
            txtTenPhim.setText(p.getTenPhim());
            txtNhaSX.setText(p.getNhaSanXuat());
            cboTheLoai.setSelectedItem(p.getTheLoai());
            txtThoiLuong.setText(String.valueOf(p.getThoiLuong()));
            txtQuocGia.setText(p.getQuocGia());
            txtMaPhim.setEditable(false); 
        }
    }

    private void xoaRong() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtNhaSX.setText("");
        cboTheLoai.setSelectedIndex(0);
        txtThoiLuong.setText("");
        txtQuocGia.setText("");
        txtTimPhim.setText("");
        table.clearSelection();
        txtMaPhim.setEditable(true);
        txtMaPhim.requestFocus();
    }
}