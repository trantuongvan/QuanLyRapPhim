package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import dao.QuanLyRap_DAO;
import entity.SuatChieu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import entity.LoadData;
import entity.Phim;
import entity.Rap;

public class QuanLySuatChieu extends JPanel implements ActionListener, LoadData {
    private QuanLySuatChieu_DAO quanLySuatChieu_DAO;
    private JTable tblSuatChieu;
    private JTextField txtMaSuat, txtNgayChieu, txtThoiGian, txtGiaVe, txtTimSuat, txtMaPhim;
    private JComboBox<String> cbPhong, cbTenPhim;
    private JButton btnThem, btnSua, btnXoa, btnXoaRong, btnTim;
    private DefaultTableModel model;
    private JTree treeNgayChieu;
    protected QuanLyPhim_DAO dataPhim;
    private QuanLyRap_DAO dataRap;

    @Override
    public void loadData() {
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        // Thêm tên phim vào cb
        layTenPhim();
        // Hiển thị dữ liệu lên bảng
        capNhatBang();
    }

    public QuanLySuatChieu() {
        // ===== Cấu hình =====
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ===== Khởi tạo DAO =====
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();

        // ===== Tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ SUẤT CHIẾU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(new Color(220, 20, 60));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Panel trái: Cây tháng =====
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

        // ===== Panel trung tâm =====
        JPanel pnBody = new JPanel(new BorderLayout());
        pnBody.setBackground(Color.WHITE);
        pnBody.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(pnBody, BorderLayout.CENTER);

        // ===== Form nhập liệu =====
        JPanel pnForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pnForm.setBackground(Color.WHITE);

        pnForm.add(new JLabel("Mã Suất Chiếu:"));
        txtMaSuat = new JTextField();
        pnForm.add(txtMaSuat);

        pnForm.add(new JLabel("Mã Phim:"));
        txtMaPhim = new JTextField();
        txtMaPhim.setEditable(false);
        pnForm.add(txtMaPhim);

        pnForm.add(new JLabel("Tên Phim:"));
        cbTenPhim = new JComboBox<>();
        layTenPhim();
        pnForm.add(cbTenPhim);

        pnForm.add(new JLabel("Giá vé:"));
        txtGiaVe = new JTextField();
        pnForm.add(txtGiaVe);

        pnForm.add(new JLabel("Ngày Chiếu (dd/MM/yyyy):"));
        txtNgayChieu = new JTextField();
        pnForm.add(txtNgayChieu);

        pnForm.add(new JLabel("Thời Gian (HH:mm):"));
        txtThoiGian = new JTextField();
        pnForm.add(txtThoiGian);

        pnForm.add(new JLabel("Phòng chiếu:"));
        cbPhong = new JComboBox<>();
        // Load phòng (rạp) từ database
        layPhong();
        pnForm.add(cbPhong);

        pnBody.add(pnForm, BorderLayout.NORTH);

        // ===== Bảng suất chiếu =====
        model = new DefaultTableModel(
                new String[] { "Mã Suất", "Mã Phim", "Tên Phim", "Phòng", "Ngày chiếu", "Thời gian", "Giá vé" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblSuatChieu = new JTable(model);
        tblSuatChieu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tblSuatChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        tblSuatChieu.setRowHeight(26);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chonSuatChieu();
        pnBody.add(new JScrollPane(tblSuatChieu), BorderLayout.CENTER);

        // ===== Panel dưới: nút chức năng =====
        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnSouth.setBackground(Color.WHITE);

        JLabel lblTim = new JLabel("Tìm mã suất:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTimSuat = new JTextField(15);
        txtTimSuat.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        btnTim = new JButton("Tìm");
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaRong = new JButton("Xóa rỗng");

        JButton[] arrBtns = { btnThem, btnSua, btnXoa, btnXoaRong, btnTim };
        Color[] colors = {
                new Color(46, 204, 113), // xanh lá
                new Color(52, 152, 219), // xanh dương
                new Color(231, 76, 60), // đỏ
                new Color(155, 89, 182), // tím
                new Color(241, 196, 15), // vàng
                new Color(255, 140, 0) // cam
        };

        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);
        for (int i = 0; i < arrBtns.length; i++) {
            arrBtns[i].setFont(btnFont);
            arrBtns[i].setBackground(colors[i]);
            arrBtns[i].setForeground(Color.WHITE);
            arrBtns[i].setFocusPainted(false);
            arrBtns[i].setPreferredSize(new Dimension(130, 45));
            // Đăng ký listener cho các nút để actionPerformed được gọi
            arrBtns[i].addActionListener(this);
        }

        pnSouth.add(lblTim);
        pnSouth.add(txtTimSuat);
        pnSouth.add(btnTim);
        pnSouth.add(btnThem);
        pnSouth.add(btnSua);
        pnSouth.add(btnXoa);
        pnSouth.add(btnXoaRong);

        add(pnSouth, BorderLayout.SOUTH);

        // ===== Gọi hàm hiển thị dữ liệu =====
        capNhatBang();
        chonNut();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            them();
        } else if (src == btnSua) {
            sua();
        } else if (src == btnXoa) {
            xoa();
        } else if (src == btnXoaRong) {
            xoaRong();
        } else if (src == btnTim) {
            tim();
        }
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
                    String month = text.replace("Tháng:", "").trim();
                    capNhatBangTheoThang(month);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Định dạng tháng không hợp lệ!");
                }
            } else {
                capNhatBang();
            }
        });
    }

    // ================= Các hàm xử lý ====================
    private String tenPhim(String maPhim) {
        dataPhim = new QuanLyPhim_DAO();
        Phim phim = dataPhim.timPhimTheoMa(maPhim);
        // return dataPhim.timPhimTheoMa(maPhim).getTenPhim();
        if (phim != null) {
            return phim.getTenPhim();
        }
        return null;
    }

    private void chonSuatChieu() {
        tblSuatChieu.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return; // tránh xử lý 2 lần
            int row = tblSuatChieu.getSelectedRow();
            if (row < 0)
                return;
            try {
                String maSuat = String.valueOf(model.getValueAt(row, 0));
                String maPhim = String.valueOf(model.getValueAt(row, 1));
                String tenPhim = model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "";
                String maRap = model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "";
                String ngay = model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "";
                String gio = model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "";
                String gia = model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "";

                txtMaSuat.setText(maSuat);
                txtMaPhim.setText(maPhim);
                // chọn tên phim trong combobox (nếu tồn tại)
                if (tenPhim != null && !tenPhim.isEmpty()) {
                    cbTenPhim.setSelectedItem(tenPhim);
                }
                txtNgayChieu.setText(ngay);
                txtThoiGian.setText(gio);
                txtGiaVe.setText(gia);

                // Chọn phòng tương ứng trong cbPhong. cbPhong chứa các mục dạng "ma - ten"
                boolean found = false;
                for (int i = 0; i < cbPhong.getItemCount(); i++) {
                    String item = cbPhong.getItemAt(i);
                    if (item != null) {
                        // kiểm tra bắt đầu bằng mã
                        if (item.startsWith(maRap + " -") || item.startsWith(maRap + "-")
                                || item.startsWith(maRap + " ")) {
                            cbPhong.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                        // hoặc chứa mã ở đầu nếu có khoảng trắng khác
                        if (item.startsWith(maRap)) {
                            cbPhong.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    // nếu không tìm thấy, giữ nguyên hoặc chọn mục đầu
                    if (cbPhong.getItemCount() > 0)
                        cbPhong.setSelectedIndex(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void layTenPhim() {
        try {
            dataPhim = new QuanLyPhim_DAO();
            ArrayList<Phim> danhSach = dataPhim.getAllPhim();
            cbTenPhim.removeAllItems();
            // thêm mục placeholder ở vị trí đầu
            String placeholder = "---:---";
            cbTenPhim.addItem(placeholder);

            if (danhSach == null || danhSach.isEmpty()) {
                // nếu chưa có phim, thêm thông báo nhưng giữ placeholder ở đầu
                cbTenPhim.addItem("(Chưa có phim)");
                cbTenPhim.setSelectedIndex(0);
                return;
            }
            for (Phim p : danhSach) {
                cbTenPhim.addItem(p.getTenPhim());
            }
            // đặt mặc định chọn placeholder
            cbTenPhim.setSelectedIndex(0);
            // khi chọn 1 phim thì tự động điền mã phim
            cbTenPhim.addActionListener(e -> {
                String selectedTenPhim = (String) cbTenPhim.getSelectedItem();
                if (selectedTenPhim == null || selectedTenPhim.equals(placeholder)
                        || selectedTenPhim.equals("(Chưa có phim)") || selectedTenPhim.equals("(Lỗi tải phim)")) {
                    txtMaPhim.setText("");
                    return;
                }
                for (Phim p : danhSach) {
                    if (p.getTenPhim().equals(selectedTenPhim)) {
                        txtMaPhim.setText(p.getMaPhim());
                        break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            cbTenPhim.removeAllItems();
            cbTenPhim.addItem("(Lỗi tải phim)");
        }
    }

    private void capNhatBang() {
        model.setRowCount(0);
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            model.addRow(new Object[] {
                    suat.getMaSuatChieu(),
                    suat.getMaPhim(),
                    tenPhim(suat.getMaPhim()),
                    suat.getMaRap(),
                    suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    suat.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                    suat.getGiaVe()
            });
        }
    }

    private void capNhatBangTheoThang(String monthStr) {
        model.setRowCount(0);
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            // lọc theo tháng
            if (suat.getNgayChieu().getMonthValue() == Integer.parseInt(monthStr)) {
                model.addRow(new Object[] {
                        suat.getMaSuatChieu(),
                        suat.getMaPhim(),
                        tenPhim(suat.getMaPhim()),
                        suat.getMaRap(),
                        suat.getNgayChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        suat.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm")),
                        suat.getGiaVe()
                });
            }
        }
    }

    // Load danh sách phòng (rạp) từ database vào combobox
    private void layPhong() {

        try {
            dataRap = new QuanLyRap_DAO();
            ArrayList<Rap> danhSach = dataRap.getAllRap();
            cbPhong.removeAllItems();
            // thêm mục placeholder ở vị trí đầu
            String placeholder = "---:---";
            cbPhong.addItem(placeholder);

            if (danhSach == null || danhSach.isEmpty()) {
                // nếu chưa có phòng, thêm thông báo nhưng giữ placeholder ở đầu
                cbPhong.addItem("(Chưa có phòng)");
                cbPhong.setSelectedIndex(0);
                return;
            }
            for (Rap r : danhSach) {
                cbPhong.addItem(r.getMaRap() + " - " + r.getTenRap());
            }
            // đặt mặc định chọn placeholder
            cbPhong.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
            cbPhong.removeAllItems();
            cbPhong.addItem("(Lỗi tải phòng)");
        }
    }

    private void tim() {
        String maSuatTim = txtTimSuat.getText().trim();
        if (maSuatTim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất cần tìm.");
            return;
        }
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSuat = String.valueOf(model.getValueAt(i, 0));
            if (maSuat.equalsIgnoreCase(maSuatTim)) {
                tblSuatChieu.setRowSelectionInterval(i, i);
                tblSuatChieu.scrollRectToVisible(new Rectangle(tblSuatChieu.getCellRect(i, 0, true)));
                found = true;
                break;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu với mã: " + maSuatTim);
        }
    }

    private void xoaRong() {

        txtMaSuat.setText("");
        txtMaPhim.setText("");
        if (cbTenPhim.getItemCount() > 0)
            cbTenPhim.setSelectedIndex(0);
        txtNgayChieu.setText("");
        txtThoiGian.setText("");
        txtGiaVe.setText("");
        if (cbPhong.getItemCount() > 0)
            cbPhong.setSelectedIndex(0);
        tblSuatChieu.clearSelection();
    }

    private void xoa() {
        int selectedRow = tblSuatChieu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu cần xóa.");
            return;
        }
        String maSuat = String.valueOf(model.getValueAt(selectedRow, 0));
        SuatChieu suatChieu = quanLySuatChieu_DAO.timSuatChieu(maSuat);
        quanLySuatChieu_DAO.removeSuatChieu(suatChieu);

        model.removeRow(selectedRow);
        xoaRong();
    }

    private void sua() {
        String maSuat = txtMaSuat.getText().trim();
        String maPhim = txtMaPhim.getText().trim();

        // Validate required fields early
        if (maSuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất.");
            return;
        }
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim.");
            return;
        }

        Object phongObj = cbPhong.getSelectedItem();
        if (phongObj == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu.");
            return;
        }

        String phongStr = phongObj.toString();
        String ngayChieuStr = txtNgayChieu.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String giaVeStr = txtGiaVe.getText().trim();

        try {
            // UI uses dd/MM/yyyy (label shows this); parse accordingly
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime thoiGian = LocalTime.parse(thoiGianStr, DateTimeFormatter.ofPattern("HH:mm"));
            float giaVe = Float.parseFloat(giaVeStr);

            String maRap = phongStr.contains("-") ? phongStr.split("-")[0].trim() : phongStr.trim();

            SuatChieu suatChieu = new SuatChieu(maSuat, maPhim, maRap, ngayChieu, thoiGian, giaVe);

            // kiểm tra khoảng cách thời gian với các suất khác trong cùng phòng cùng ngày
            SuatChieu conflict = KiemTraSuatChieu(maRap, ngayChieu, thoiGian, maSuat);
            if (conflict != null) {
                JOptionPane.showMessageDialog(this,
                        "Xung đột giờ chiếu với suất " + conflict.getMaSuatChieu() + " lúc "
                                + conflict.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " — các suất phải cách nhau ít nhất 3 giờ.");
                return;
            }

            // Cập nhật xuống database qua DAO
            boolean updated = quanLySuatChieu_DAO.updateSuatChieu(suatChieu);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Cập nhật suất chiếu thành công.");
                // Cập nhật lại bảng từ DB
                capNhatBang();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật suất chiếu thất bại. Kiểm tra kết nối DB hoặc dữ liệu trùng.");
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng ngày/thời gian không hợp lệ. Ngày phải là dd/MM/yyyy, thời gian HH:mm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Vui lòng nhập số.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật suất chiếu: " + ex.getMessage());
        }
    }

    private void them() {
        String maSuat = txtMaSuat.getText().trim();
        String maPhim = txtMaPhim.getText().trim();

        // Validate required fields early
        if (maSuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã suất.");
            return;
        }
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim.");
            return;
        }

        Object phongObj = cbPhong.getSelectedItem();
        if (phongObj == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng chiếu.");
            return;
        }

        String phongStr = phongObj.toString();
        String ngayChieuStr = txtNgayChieu.getText().trim();
        String thoiGianStr = txtThoiGian.getText().trim();
        String giaVeStr = txtGiaVe.getText().trim();

        // === Biểu thức chính quy (có thể điều chỉnh theo qui tắc dự án) ===
        // Mã suất: chữ/ số/ gạch ngang/underscore, 1-20 ký tự. Thay đổi tuỳ ý.
        String reMaSuat = "^[A-Za-z0-9_-]{1,20}$";
        // Ngày dd/MM/yyyy
        String reNgay = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        // Giờ HH:mm (00-23:00-59)
        String reGio = "^([01]?\\d|2[0-3]):[0-5]\\d$";
        // Giá vé: số nguyên hoặc thập phân (tối đa 2 chữ số thập phân)
        String reGiaVe = "^\\d+(\\\\.\\d{1,2})?$";

        if (!maSuat.matches(reMaSuat)) {
            JOptionPane.showMessageDialog(this, "Mã suất không hợp lệ. Chỉ chữ, số, '-' hoặc '_' (1-20 ký tự).");
            return;
        }
        if (!ngayChieuStr.matches(reNgay)) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy.");
            return;
        }
        // giờ chiếu của các phim cùng một rap phải cách nhau 3h
        if (!thoiGianStr.matches(reGio)) {
            JOptionPane.showMessageDialog(this,
                    "Thời gian không hợp lệ. Vui lòng nhập theo định dạng HH:mm (00-23:59).");
            return;
        }
        if (!giaVeStr.matches(reGiaVe)) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Nhập số, có thể có 1-2 chữ số thập phân.");
            return;
        }

        try {
            // UI uses dd/MM/yyyy (label shows this); parse accordingly
            LocalDate ngayChieu = LocalDate.parse(ngayChieuStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime thoiGian = LocalTime.parse(thoiGianStr, DateTimeFormatter.ofPattern("HH:mm"));
            float giaVe = Float.parseFloat(giaVeStr);

            String maRap = phongStr.contains("-") ? phongStr.split("-")[0].trim() : phongStr.trim();

            SuatChieu suatChieu = new SuatChieu(maSuat, maPhim, maRap, ngayChieu, thoiGian, giaVe);

            // kiểm tra khoảng cách thời gian với các suất khác trong cùng phòng cùng ngày
            SuatChieu conflict = KiemTraSuatChieu(maRap, ngayChieu, thoiGian, null);
            if (conflict != null) {
                JOptionPane.showMessageDialog(this,
                        "Xung đột giờ chiếu với suất " + conflict.getMaSuatChieu() + " lúc "
                                + conflict.getGioChieu().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " — các suất phải cách nhau ít nhất 3 giờ.");
                return;
            }

            // Ghi xuống database qua DAO
            boolean added = quanLySuatChieu_DAO.addNewSuatChieu(suatChieu);
            if (added) {
                JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công.");
                // Cập nhật lại bảng từ DB
                capNhatBang();
                xoaRong();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm suất chiếu thất bại. Kiểm tra kết nối DB hoặc dữ liệu trùng.");
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng ngày/thời gian không hợp lệ. Ngày phải là dd/MM/yyyy, thời gian HH:mm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá vé không hợp lệ. Vui lòng nhập số.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm suất chiếu: " + ex.getMessage());
        }
    }

    private SuatChieu KiemTraSuatChieu(String maRap, LocalDate ngay, LocalTime gio, String excludeMaSuat) {
        if (maRap == null || ngay == null || gio == null)
            return null;
        ArrayList<SuatChieu> ds = quanLySuatChieu_DAO.getAllSuatChieu();
        if (ds == null || ds.isEmpty())
            return null;
        for (int i = 0; i < ds.size(); i++) {
            SuatChieu s = ds.get(i);
            if (s == null)
                continue;
            if (excludeMaSuat != null && excludeMaSuat.equals(s.getMaSuatChieu()))
                continue;
            if (s.getMaRap() == null || !s.getMaRap().equals(maRap))
                continue;
            if (!s.getNgayChieu().isEqual(ngay))
                continue;
            long diffMinutes = Math.abs(Duration.between(s.getGioChieu(), gio).toMinutes());
            if (diffMinutes < 180)
                return s;
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Quản Lý Suất Chiếu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new QuanLySuatChieu());
        frame.setVisible(true);
    }
}