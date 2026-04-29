package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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

public class QuanLyPhim extends JPanel implements LoadData, ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaPhim, txtTenPhim, txtNhaSX, txtThoiLuong, txtQuocGia, txtTimPhim;
    private JComboBox<TheLoaiPhim> cboTheLoai;
    private JButton btnThem, btnSua, btnXoa, btnXoaRong, btnLamMoi, btnTim;

    private QuanLyPhim_DAO phimDAO;
    private ArrayList<Phim> dsPhim;

    private final Font FONT_LBL = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_TXT = new Font("Segoe UI", Font.PLAIN, 16);
    private final Border BORDER_BTN = BorderFactory.createLineBorder(new Color(0, 123, 255), 1);

    public QuanLyPhim() {
        setLayout(null);
        
        Color bgColor = new Color(235, 245, 255);
        setBackground(bgColor);

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Không thể kết nối CSDL: " + e.getMessage());
        }

        phimDAO = new QuanLyPhim_DAO();

        JLabel lblTieuDe = new JLabel("Quản lý phim");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 15, 1180, 35);
        add(lblTieuDe);
        
        JPanel pnInput = new JPanel();
        pnInput.setLayout(null);
        pnInput.setBackground(Color.WHITE);
        pnInput.setBorder(BorderFactory.createTitledBorder("Thông tin phim"));
        pnInput.setBounds(40, 60, 780, 240); 
        add(pnInput);

        JLabel lblMaPhim = new JLabel("Mã phim:");
        lblMaPhim.setBounds(40, 40, 120, 30);
        lblMaPhim.setFont(FONT_LBL);
        pnInput.add(lblMaPhim);

        txtMaPhim = new JTextField();
        txtMaPhim.setBounds(170, 40, 200, 30);
        txtMaPhim.setEditable(false);
        styleTextField(txtMaPhim);
        pnInput.add(txtMaPhim);

        JLabel lblTenPhim = new JLabel("Tên phim:");
        lblTenPhim.setBounds(410, 40, 120, 30);
        lblTenPhim.setFont(FONT_LBL);
        pnInput.add(lblTenPhim);

        txtTenPhim = new JTextField();
        txtTenPhim.setBounds(540, 40, 200, 30);
        styleTextField(txtTenPhim);
        pnInput.add(txtTenPhim);
        
        JLabel lblNhaSX = new JLabel("Nhà sản xuất:");
        lblNhaSX.setBounds(40, 100, 120, 30);
        lblNhaSX.setFont(FONT_LBL);
        pnInput.add(lblNhaSX);

        txtNhaSX = new JTextField();
        txtNhaSX.setBounds(170, 100, 200, 30);
        styleTextField(txtNhaSX);
        pnInput.add(txtNhaSX);

        JLabel lblTheLoai = new JLabel("Thể loại:");
        lblTheLoai.setBounds(410, 100, 120, 30);
        lblTheLoai.setFont(FONT_LBL);
        pnInput.add(lblTheLoai);

        cboTheLoai = new JComboBox<>(TheLoaiPhim.values());
        cboTheLoai.setBounds(540, 100, 200, 30);
        cboTheLoai.setFont(FONT_TXT);
        pnInput.add(cboTheLoai);

        JLabel lblThoiLuong = new JLabel("Thời lượng:");
        lblThoiLuong.setBounds(40, 160, 120, 30);
        lblThoiLuong.setFont(FONT_LBL);
        pnInput.add(lblThoiLuong);

        txtThoiLuong = new JTextField();
        txtThoiLuong.setBounds(170, 160, 200, 30);
        styleTextField(txtThoiLuong);
        pnInput.add(txtThoiLuong);
        
        JLabel lblQuocGia = new JLabel("Quốc gia:");
        lblQuocGia.setBounds(410, 160, 120, 30);
        lblQuocGia.setFont(FONT_LBL);
        pnInput.add(lblQuocGia);

        txtQuocGia = new JTextField();
        txtQuocGia.setBounds(540, 160, 200, 30);
        styleTextField(txtQuocGia);
        pnInput.add(txtQuocGia);

        JPanel pnActions = new JPanel();
        pnActions.setLayout(null);
        pnActions.setBackground(Color.WHITE);
        
        pnActions.setBounds(840, 60, 320, 240);
        add(pnActions);

        JLabel lblTim = new JLabel("Nhập mã phim cần tìm:");
        lblTim.setFont(FONT_LBL);
        lblTim.setBounds(20, 10, 180, 30);
        pnActions.add(lblTim);

        txtTimPhim = new JTextField();
        txtTimPhim.setBounds(20, 45, 180, 30);
        styleTextField(txtTimPhim);
        pnActions.add(txtTimPhim);

        btnTim = new JButton("Tìm");
        btnTim.setIcon(new ImageIcon("icon/search.png"));
        styleButton(btnTim, new Color(108, 117, 125));
        btnTim.setBounds(210, 45, 90, 30);
        pnActions.add(btnTim);

        btnThem = new JButton("Thêm");
        btnThem.setIcon(new ImageIcon("icon/add.png"));
        styleButton(btnThem, new Color(0, 123, 255));
        btnThem.setBounds(20, 90, 135, 45); 
        pnActions.add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setIcon(new ImageIcon("icon/edit.png"));
        styleButton(btnSua, new Color(23, 162, 184));
        btnSua.setBounds(165, 90, 135, 45); 
        pnActions.add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setIcon(new ImageIcon("icon/delete.png"));
        styleButton(btnXoa, new Color(220, 53, 69));
        btnXoa.setBounds(20, 145, 135, 45); 
        pnActions.add(btnXoa);
        
        btnXoaRong = new JButton("Xóa rỗng");
        btnXoaRong.setIcon(new ImageIcon("icon/clear.png"));
        styleButton(btnXoaRong, new Color(255, 193, 7));
        btnXoaRong.setBounds(165, 145, 135, 45); 
        pnActions.add(btnXoaRong);
        
        btnLamMoi = new JButton("Làm mới Table");
        btnLamMoi.setIcon(new ImageIcon("icon/refresh.png"));
        styleButton(btnLamMoi, new Color(108, 117, 125));
        btnLamMoi.setBounds(20, 200, 280, 35); 
        pnActions.add(btnLamMoi);
        
        model = new DefaultTableModel(new String[] {
                "Mã phim", "Tên phim", "Nhà SX", "Thể loại", "Thời lượng", "Quốc gia"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

        JScrollPane scroll = new JScrollPane(table);
        
        scroll.setBounds(40, 320, 1120, 400); 
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
    
    private void styleTextField(JTextField txt) {
        txt.setFont(FONT_TXT);
        txt.setMargin(new Insets(2, 6, 2, 6));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
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
                JOptionPane.showMessageDialog(this, "✅ Thêm phim thành công!");
                loadDataToTable();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!");
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
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                loadDataToTable();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim cần sửa!");
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
        
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa phim " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (phimDAO.xoaPhim(ma)) {
                    JOptionPane.showMessageDialog(this, "✅ Xóa thành công!");
                    loadDataToTable();
                    xoaRong();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim cần xóa!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Không thể xóa phim này!\nPhim đã có vé được bán trong hóa đơn.",
                    "Lỗi CSDL", 
                    JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim có mã " + ma);
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