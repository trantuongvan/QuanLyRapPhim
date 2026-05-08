package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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

    public QuanLyPhim() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        Color darkBg = new Color(34, 34, 34);
        setBackground(darkBg);

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }

        phimDAO = new QuanLyPhim_DAO();

        Color orangeColor = new Color(245, 140, 0);
        Color redColor = new Color(175, 25, 25);

        JLabel lblTieuDe = new JLabel("Quản lý phim");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnCenter = new JPanel(new BorderLayout(0, 20));
        pnCenter.setOpaque(false);
        add(pnCenter, BorderLayout.CENTER);

        JPanel pnTopAndForm = new JPanel(new BorderLayout(0, 20));
        pnTopAndForm.setOpaque(false);
        pnCenter.add(pnTopAndForm, BorderLayout.NORTH);

        //CRUD
        JPanel pnTop = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        pnTop.setOpaque(false);
        pnTop.setBorder(new EmptyBorder(12, 15, 12, 15));

        txtTimPhim = new JTextField() {
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
        txtTimPhim.setOpaque(false);
        txtTimPhim.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtTimPhim.setFont(FONT_TXT);
        txtTimPhim.setForeground(Color.BLACK);
        txtTimPhim.setCaretColor(Color.BLACK);
        txtTimPhim.setPreferredSize(new Dimension(150, 35));
        txtTimPhim.setMinimumSize(new Dimension(150, 35));

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
        pnTop.add(txtTimPhim, gbcTop);

        gbcTop.weightx = 0.0;
        gbcTop.gridx = 2;
        pnTop.add(btnTim, gbcTop);
        gbcTop.gridx = 3;
        pnTop.add(btnThem, gbcTop);
        gbcTop.gridx = 4;
        pnTop.add(btnSua, gbcTop);
        gbcTop.gridx = 5;
        pnTop.add(btnXoa, gbcTop);
        gbcTop.gridx = 6;
        pnTop.add(btnXoaRong, gbcTop);
        gbcTop.gridx = 7;
        pnTop.add(btnLamMoi, gbcTop);

        pnTopAndForm.add(pnTop, BorderLayout.NORTH);

        //input
        JPanel pnInput = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.white);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnInput.setOpaque(false);
        pnInput.setBorder(new EmptyBorder(15, 20, 20, 20));

        JLabel lblThongTin = new JLabel("THÔNG TIN PHIM");
        lblThongTin.setHorizontalAlignment(SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblThongTin.setForeground(Color.BLACK);
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
                    super.paintComponent(g);
                }
            };
            tfs[i].setOpaque(false);
            tfs[i].setBorder(new EmptyBorder(0, 10, 0, 10));
            tfs[i].setForeground(Color.BLACK);
            tfs[i].setFont(FONT_TXT);
            tfs[i].setCaretColor(Color.BLACK);
            tfs[i].setPreferredSize(new Dimension(0, 35));
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
        cboTheLoai.setPreferredSize(new Dimension(0, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Mã phim:", FONT_LBL), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtMaPhim, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Tên phim:", FONT_LBL), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0; pnForm.add(txtTenPhim, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Nhà sản xuất:", FONT_LBL), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtNhaSX, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Thể loại:", FONT_LBL), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnForm.add(cboTheLoai, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Thời lượng:", FONT_LBL), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnForm.add(txtThoiLuong, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnForm.add(taoLabel("Quốc gia:", FONT_LBL), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnForm.add(txtQuocGia, gbc);

        pnInput.add(pnForm, BorderLayout.CENTER);
        pnTopAndForm.add(pnInput, BorderLayout.CENTER);


        //table
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
        table.getTableHeader().setBackground(redColor);
        table.getTableHeader().setForeground(Color.WHITE);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setOpaque(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        pnCenter.add(scroll, BorderLayout.CENTER);

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