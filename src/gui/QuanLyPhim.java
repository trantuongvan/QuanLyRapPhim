package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ConnectDB.ConnectDB;
import dao.QuanLyPhim_DAO;
import entity.Phim;
import entity.TheLoaiPhim;
import entity.LoadData;

public class QuanLyPhim extends JPanel implements LoadData, ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaPhim, txtTenPhim, txtNhaSX, txtThoiLuong, txtQuocGia, txtTimPhim;
    private JComboBox<TheLoaiPhim> cboTheLoai;
    private JButton btnThem, btnSua, btnXoa, btnXoaRong, btnLamMoi, btnTim;

    private QuanLyPhim_DAO phimDAO;
    private ArrayList<Phim> dsPhim;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 14);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    public QuanLyPhim() {
        phimDAO = new QuanLyPhim_DAO();
        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ PHIM");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        gbc.gridy = 1;
        JPanel pnTop = createStyledPanel();
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        txtTimPhim = createStyledTextField(300);
        btnTim = taoNutBoGoc("Tìm", new Color(160, 82, 45));
        btnThem = taoNutBoGoc("Thêm", orangeColor);
        btnSua = taoNutBoGoc("Sửa", orangeColor);
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLamMoi = taoNutBoGoc("Làm mới", orangeColor);

        JLabel lblTim = new JLabel("Tìm mã:");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim);
        pnTop.add(txtTimPhim);
        pnTop.add(btnTim);
        pnTop.add(btnThem);
        pnTop.add(btnSua);
        pnTop.add(btnXoa);
        pnTop.add(btnXoaRong);
        pnTop.add(btnLamMoi);
        add(pnTop, gbc);

        gbc.gridy = 2;
        JPanel pnInput = createStyledPanel();
        pnInput.setLayout(new GridBagLayout());
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 20, 10, 20);
        innerGbc.anchor = GridBagConstraints.WEST;

        innerGbc.gridx = 0; innerGbc.gridy = 0; innerGbc.gridwidth = 4;
        innerGbc.anchor = GridBagConstraints.CENTER;
        JLabel lblHeader = new JLabel("THÔNG TIN CHI TIẾT PHIM");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        pnInput.add(lblHeader, innerGbc);

        innerGbc.gridwidth = 1; innerGbc.anchor = GridBagConstraints.WEST;

        txtMaPhim = createStyledTextField(200);
        txtTenPhim = createStyledTextField(200);
        txtNhaSX = createStyledTextField(200);
        txtThoiLuong = createStyledTextField(200);
        txtQuocGia = createStyledTextField(200);

        cboTheLoai = new JComboBox<>(TheLoaiPhim.values());
        cboTheLoai.setPreferredSize(new Dimension(200, 35));
        cboTheLoai.setBackground(orangeColor);

        addRow(pnInput, innerGbc, 1, "Mã phim:", txtMaPhim, "Tên phim:", txtTenPhim);
        addRow(pnInput, innerGbc, 2, "Nhà sản xuất:", txtNhaSX, "Thể loại:", cboTheLoai);
        addRow(pnInput, innerGbc, 3, "Thời lượng:", txtThoiLuong, "Quốc gia:", txtQuocGia);

        add(pnInput, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        model = new DefaultTableModel(new String[] {
                "Mã phim", "Tên phim", "Nhà sản xuất", "Thể loại", "Thời lượng", "Quốc gia"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(35);
        table.setBackground(tableBackground);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 70, 70));
        table.setSelectionBackground(new Color(80, 80, 80));
        table.setSelectionForeground(orangeColor);

        table.getTableHeader().setBackground(new Color(175, 25, 25));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(tableBackground);
        add(scroll, gbc);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());

        loadData();
    }

    private JPanel createStyledPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
    }

    private void addRow(JPanel p, GridBagConstraints c, int row, String l1, JComponent f1, String l2, JComponent f2) {
        c.gridy = row;
        c.gridx = 0; c.weightx = 0; p.add(createFieldLabel(l1), c);
        c.gridx = 1; c.weightx = 0.5; c.fill = GridBagConstraints.HORIZONTAL; p.add(f1, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE; p.add(createFieldLabel(l2), c);
        c.gridx = 3; c.weightx = 0.5; c.fill = GridBagConstraints.HORIZONTAL; p.add(f2, c);
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(FONT_LBL);
        return l;
    }

    private JTextField createStyledTextField(int width) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(orangeColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(0, 10, 0, 10));
        tf.setFont(FONT_TXT);
        tf.setForeground(Color.BLACK);
        tf.setPreferredSize(new Dimension(width, 35));
        return tf;
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
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
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