package gui;

import javax.swing.*;

import dao.QuanLyGhe_DAO;
import dao.QuanLyHoaDon_DAO;
import dao.QuanLyKhachHang_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLyRap_DAO;
import dao.QuanLySuatChieu_DAO;
import dao.QuanLyVe_DAO;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import entity.*;

public class QuanLyBanVe extends JPanel implements LoadData, ResetForm {
    private JComboBox<String> cbPhim, cbPhong, cbSuatChieu, cbGioiTinh;
    private JButton btnChonGhe, btnDatVe, btnXoaChon;
    private JTextField txtHoTen, txtSDT, txtDiaChi;
    private JTextField txtTenPhim, txtTenPhong, txtThoiLuong, txtTheLoai, txtSuatChieu, txtSoGhe;
    private SuatChieu suatChieuDuocChon;
    private ArrayList<String> roomIDSelectList;
    private ArrayList<String> showtimeSelectList;
    private ArrayList<String> selectedChairs;

    private QuanLyPhim_DAO movieManager = new QuanLyPhim_DAO();
    private QuanLySuatChieu_DAO suatChieuManager = new QuanLySuatChieu_DAO();
    private QuanLyRap_DAO rapManager = new QuanLyRap_DAO();
    private QuanLyGhe_DAO chairManager = new QuanLyGhe_DAO();
    private QuanLyKhachHang_DAO customerManager = new QuanLyKhachHang_DAO();
    private QuanLyHoaDon_DAO billManager = new QuanLyHoaDon_DAO();
    private QuanLyVe_DAO ticketManager = new QuanLyVe_DAO();

    private Dimension modalDimension = new Dimension(500, 600);
    private Font fChonGhe;
    private ArrayList<Phim> movieList;
    private final Color orangeColor = new Color(245, 140, 0);

    public QuanLyBanVe() {
        Color bgMain = new Color(30, 30, 30);
        Color bgSecondary = new Color(45, 45, 45);
        Color textColor = Color.WHITE;


        setLayout(new BorderLayout(10, 10));
        setBackground(bgMain);

        loadData();
        JLabel lblTitle = new JLabel("QUẢN LÝ BÁN VÉ", JLabel.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));

        lblTitle.setForeground(orangeColor);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBackground(bgMain);

        this.cbPhong = new JComboBox<>(new String[] { "---Chọn phòng---" });
        this.cbSuatChieu = new JComboBox<>(new String[] { "---Chọn suất chiếu---" });
        styleComboBox(cbPhong);
        styleComboBox(cbSuatChieu);

        JPanel pChonPhim = createStyledSection("CHỌN VÉ XEM PHIM", bgSecondary, textColor);

        JPanel pPhim = createRow(new JLabel("Chọn phim:"), cbPhim = new JComboBox<>(), bgSecondary, textColor);
        styleComboBox(cbPhim);
        JPanel pPhong = createRow(new JLabel("Chọn phòng:"), cbPhong, bgSecondary, textColor);
        btnChonGhe = new JButton("Chọn ghế");
        JPanel suatComboAndButton = new JPanel(new BorderLayout(5, 0));
        suatComboAndButton.setOpaque(false);
        suatComboAndButton.add(cbSuatChieu, BorderLayout.CENTER);
        suatComboAndButton.add(btnChonGhe, BorderLayout.EAST);
        JPanel pSuatRow = createRow(new JLabel("Chọn suất chiếu:"), suatComboAndButton, bgSecondary, textColor);

        this.fChonGhe = new Font("Tahoma", Font.BOLD, 16);
        btnChonGhe.setBackground(Color.RED);
        btnChonGhe.setForeground(Color.WHITE);
        btnChonGhe.setFont(fChonGhe);

        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pPhim);
        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pPhong);
        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pSuatRow);
        pChonPhim.add(Box.createVerticalStrut(20));
        JPanel pGridContainer = new JPanel(new GridLayout(3, 1, 0, 10));
        pGridContainer.setBackground(bgMain);
        pGridContainer.add(pChonPhim);

        JPanel infoPanel = createStyledSection("THÔNG TIN KHÁCH HÀNG", bgSecondary, textColor);

        txtHoTen = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });

        styleTextField(txtHoTen);
        styleTextField(txtSDT);
        styleTextField(txtDiaChi);
        styleComboBox(cbGioiTinh);

        infoPanel.add(createRow(new JLabel("Họ tên:"), txtHoTen, bgSecondary, textColor));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createRow(new JLabel("Số điện thoại:"), txtSDT, bgSecondary, textColor));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createRow(new JLabel("Địa chỉ:"), txtDiaChi, bgSecondary, textColor));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createRow(new JLabel("Giới tính:"), cbGioiTinh, bgSecondary, textColor));

        JPanel pInfoMovie = createStyledSection("THÔNG TIN VÉ XEM PHIM", bgSecondary, textColor);

        txtTenPhim = new JTextField();
        txtTenPhong = new JTextField();
        txtThoiLuong = new JTextField();
        txtTheLoai = new JTextField();
        txtSuatChieu = new JTextField();
        txtSoGhe = new JTextField();

        JTextField[] ticketFields = {txtTenPhim, txtTenPhong, txtThoiLuong, txtTheLoai, txtSuatChieu, txtSoGhe};
        for(JTextField tf : ticketFields) {
            tf.setEditable(false);
            styleTextField(tf);
        }

        pInfoMovie.add(createRow(new JLabel("Tên phim:"), txtTenPhim, bgSecondary, textColor));
        pInfoMovie.add(createRow(new JLabel("Phòng:"), txtTenPhong, bgSecondary, textColor));
        pInfoMovie.add(createRow(new JLabel("Thời lượng:"), txtThoiLuong, bgSecondary, textColor));
        pInfoMovie.add(createRow(new JLabel("Thể loại:"), txtTheLoai, bgSecondary, textColor));
        pInfoMovie.add(createRow(new JLabel("Suất chiếu:"), txtSuatChieu, bgSecondary, textColor));
        pInfoMovie.add(createRow(new JLabel("Ghế:"), txtSoGhe, bgSecondary, textColor));

        pGridContainer.add(infoPanel);
        pGridContainer.add(pInfoMovie);
        pCenter.add(pGridContainer);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(bgMain);
        btnXoaChon = new JButton("Xóa lựa chọn");
        btnXoaChon.setFont(fChonGhe);
        btnXoaChon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaChon.setFocusPainted(false);
        btnXoaChon.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnXoaChon.setPreferredSize(new Dimension(200, 45));
        btnDatVe = new JButton("Xác nhận đặt vé");
        btnDatVe.setBackground(orangeColor);
        btnDatVe.setForeground(Color.WHITE);
        btnDatVe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDatVe.setFocusPainted(false);
        btnDatVe.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnDatVe.setPreferredSize(new Dimension(200, 45));

        btnPanel.add(btnXoaChon);
        btnPanel.add(btnDatVe);
        pCenter.add(btnPanel);

        add(pCenter, BorderLayout.CENTER);

        btnXoaChon.addActionListener(e -> resetForm());
        btnDatVe.addActionListener(e -> acceptTicket());
        btnChonGhe.addActionListener(e -> selectChair());
        cbPhim.addActionListener(e -> CapNhatThongTinPhim());
        cbPhong.addActionListener(e -> CapNhatPhong());
        cbSuatChieu.addActionListener(e -> CapNhatSuatChieu());
        txtSDT.addActionListener(e -> AutoFillCustomer());
    }

    private void styleComboBox(JComboBox cb) {
        cb.setBackground(orangeColor);
        cb.setForeground(Color.black);
        cb.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(orangeColor);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private JPanel createStyledSection(String title, Color bg, Color text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(text);
        panel.setBorder(border);
        return panel;
    }

    private JPanel createRow(JLabel label, JComponent comp, Color bg, Color text) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(bg);
        label.setForeground(text);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 10);
        label.setPreferredSize(new Dimension(120, 30));
        row.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 20);
        row.add(comp, gbc);

        return row;
    }

    @Override
    public void resetForm() {
        cbPhim.setSelectedIndex(0);
        cbPhong.setSelectedIndex(0);
        cbSuatChieu.setSelectedIndex(0);
        cbGioiTinh.setSelectedIndex(0);

        cbPhong.setEnabled(false);
        cbSuatChieu.setEnabled(false);
        btnChonGhe.setEnabled(false);
        btnDatVe.setEnabled(false);

        txtDiaChi.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");

        deleteTextMovieInfo();
        this.suatChieuDuocChon = null;
        if (this.selectedChairs != null)
            this.selectedChairs.clear();
    }

    private void acceptTicket() {
        String hoten = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String gioiTinh = cbGioiTinh.getSelectedItem().toString();
        if (hoten.equals("") || sdt.equals("") || diaChi.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin khách hàng", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String regexHoten = "^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\\s[A-ZÀ-Ỹ][a-zà-ỹ]+)+$";
        String regexSDT = "^(03|05|07|08|09)[0-9]{8}$";
        String regexDiaChi = "^[A-Ỹa-ỹ0-9/,\\s]+$";
        if (!hoten.matches(regexHoten)) {
            JOptionPane.showMessageDialog(this, "Gồm phần họ và tên, chữ cái đầu phải viết hoa", "Lỗi cú pháp Họ tên",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!sdt.matches(regexSDT)) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải có 10 số và 2 số đầu phải là 03, 05, 07,...",
                    "Lỗi cú pháp số điện thoại",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!diaChi.matches(regexDiaChi)) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được có kí tự đặc biệt.",
                    "Lỗi cú pháp địa chỉ", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String maKH = this.customerManager.taoMaKHTuDong();
        KhachHang khachHang = new KhachHang(maKH, hoten, gioiTinh, sdt, diaChi);
        customerManager.add(khachHang);

        if (this.selectedChairs == null || this.suatChieuDuocChon == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn ghế !", "Lỗi đặt vé", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Ve ve = createTicket();
        float giaVe = this.suatChieuDuocChon.getGiaVe();
        HoaDon hoaDon = xuLyTaoHoaDon(khachHang, this.selectedChairs.size(), giaVe);

        new ThongTinVeModal(hoaDon, ve, this, this.selectedChairs);
    }

    private HoaDon xuLyTaoHoaDon(KhachHang khachHang, int soLuongVe, float giaVe) {
        NhanVien nhanVien = DangNhap.nhanVienDangNhap;
        float tongTien = giaVe * soLuongVe;
        HoaDon hoaDon = new HoaDon(this.billManager.taoMaHoaDonTuDong(), LocalDate.now(), nhanVien, khachHang, tongTien);
        return hoaDon;
    }

    public Ve createTicket() {
        if (this.suatChieuDuocChon == null)
            return null;
        Ve ve = new Ve(QuanLyVe_DAO.taoMaVeTuDong());
        ve.setMaSuatChieu(this.suatChieuDuocChon.getMaSuatChieu());
        ve.setNgayBan(LocalDate.now());
        ve.setDaThanhToan(false);
        return ve;
    }

    private void deleteTextMovieInfo() {
        txtSoGhe.setText("");
        txtTenPhim.setText("");
        txtTenPhong.setText("");
        txtTheLoai.setText("");
        txtSuatChieu.setText("");
        txtThoiLuong.setText("");
    }

    private void CapNhatThongTinPhim() {
        if (this.cbPhim.getItemCount() == 0) {
            return;
        }
        if (this.cbPhim.getSelectedIndex() == 0) {
            deleteTextMovieInfo();
            this.cbPhong.setEnabled(false);
            this.cbSuatChieu.setEnabled(false);
            this.btnChonGhe.setEnabled(false);
            this.btnDatVe.setEnabled(false);
            return;
        }
        this.txtTenPhim.setText("");
        this.txtTheLoai.setText("");
        this.txtThoiLuong.setText("");
        Phim phim = this.movieList.get(this.cbPhim.getSelectedIndex() - 1);
        this.txtTenPhim.setText(phim.getTenPhim());
        if (phim.getTheLoai() != null) {
            txtTheLoai.setText(phim.getTheLoai().getTenHienThi());
        } else {
            throw new NullPointerException("Thể loại của phim " + phim.getTenPhim() + " bị null");
        }
        txtThoiLuong.setText(Integer.toString(phim.getThoiLuong()) + " phút");
        this.suatChieuDuocChon = new SuatChieu("AUTO_GENERATE");
        this.suatChieuDuocChon.setMaPhim(phim.getMaPhim());
        UnlockChooseRoom();
    }

    private void UnlockChooseRoom() {
        ArrayList<SuatChieu> dsSuatChieu = suatChieuManager.getSuatChieuTheoPhim(this.suatChieuDuocChon.getMaPhim());
        cbPhong.setEnabled(true);
        cbPhong.removeAllItems();
        cbPhong.addItem("---Chọn phòng---");
        roomIDSelectList = new ArrayList<>();
        roomIDSelectList.add("");
        HashSet<String> dsMaRap = new HashSet<>();
        for (SuatChieu suatChieu : dsSuatChieu) {
            dsMaRap.add(suatChieu.getMaRap());
        }
        for (String maRap : dsMaRap) {
            Rap rap = rapManager.findRapByID(maRap);
            if (rap != null) {
                cbPhong.addItem(rap.getTenRap());
                roomIDSelectList.add(maRap);
            }
        }
    }

    private void CapNhatPhong() {
        if (cbPhong.getItemCount() == 0)
            return;
        if (cbPhong.getSelectedIndex() == 0) {
            txtTenPhong.setText("");
            cbSuatChieu.setEnabled(false);
            btnChonGhe.setEnabled(false);
            btnDatVe.setEnabled(false);
            return;
        }
        txtTenPhong.setText("");
        txtTenPhong.setText(cbPhong.getSelectedItem().toString());
        int index = cbPhong.getSelectedIndex();
        this.suatChieuDuocChon.setMaRap(roomIDSelectList.get(index));
        UnlockChoosMovieShowtime();
    }

    private void UnlockChoosMovieShowtime() {
        ArrayList<SuatChieu> dsSuatChieu = suatChieuManager.getSuatChieuTheoPhim(this.suatChieuDuocChon.getMaPhim());
        cbSuatChieu.setEnabled(true);
        cbSuatChieu.removeAllItems();
        cbSuatChieu.addItem("---Chọn suất chiếu---");
        showtimeSelectList = new ArrayList<>();
        showtimeSelectList.add("");
        for (SuatChieu suatChieu : dsSuatChieu) {
            if (suatChieu.getMaRap().equals(this.suatChieuDuocChon.getMaRap())) {
                String dataSuatChieu = suatChieu.getNgayChieu().toString() + ", " + suatChieu.getGioChieu().toString();
                cbSuatChieu.addItem(dataSuatChieu);
                showtimeSelectList.add(suatChieu.getMaSuatChieu());
            }
        }
    }

    private void CapNhatSuatChieu() {
        if (cbSuatChieu.getItemCount() == 0)
            return;
        if (cbSuatChieu.getSelectedIndex() == 0) {
            txtSuatChieu.setText("");
            btnChonGhe.setEnabled(false);
            btnDatVe.setEnabled(false);
            return;
        }
        txtSuatChieu.setText("");
        txtSuatChieu.setText(cbSuatChieu.getSelectedItem().toString());
        int index = cbSuatChieu.getSelectedIndex();
        this.suatChieuDuocChon = this.suatChieuManager.timSuatChieu(showtimeSelectList.get(index));
        btnChonGhe.setEnabled(true);
    }

    private void selectChair() {
        if (this.suatChieuDuocChon == null || this.suatChieuDuocChon.getMaRap() == null) return;

        Rap rap = rapManager.findRapByID(suatChieuDuocChon.getMaRap());
        if (rap == null) return;

        int numCols = 5;
        int soGhe = rap.getSoLuongGhe();
        int numRows = (int) Math.ceil((double) soGhe / numCols);

        JFrame chairFrame = new JFrame("Chọn ghế");
        chairFrame.getContentPane().setBackground(new Color(10, 10, 10));
        chairFrame.setSize(800, 600);
        chairFrame.setLocationRelativeTo(this);
        chairFrame.setLayout(new BorderLayout(10, 10));

        JPanel screenPanel = new JPanel(new BorderLayout());
        screenPanel.setOpaque(false);
        JLabel lblScreen = new JLabel("Màn hình", SwingConstants.CENTER);
        lblScreen.setForeground(Color.WHITE);
        lblScreen.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblScreen.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        lblScreen.setBackground(new Color(20, 20, 20));
        chairFrame.add(lblScreen, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(numRows + 1, numCols + 1, 8, 8));
        centerPanel.setBackground(new Color(18, 18, 18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        ArrayList<Ghe> chairList = this.chairManager.getDanhSachGheTheoRap(rap);
        ArrayList<Ve> ticketList = this.ticketManager.timVeTheoMaSuatChieu(this.suatChieuDuocChon.getMaSuatChieu());
        ArrayList<String> selectedChairs = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            JLabel rowLabel = new JLabel(String.valueOf((char)('A' + r)), SwingConstants.CENTER);
            rowLabel.setForeground(Color.WHITE);
            rowLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
            centerPanel.add(rowLabel);

            for (int c = 0; c < numCols; c++) {
                int index = r * numCols + c;
                if (index < soGhe) {
                    Ghe ghe = chairList.get(index);
                    JPanel cell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                    cell.setOpaque(false);

                    JButton btn = new JButton();
                    btn.setPreferredSize(new Dimension(35, 35));
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btn.setBorder(null);

                    if (isGheDaDat(ghe, ticketList)) {
                        btn.setBackground(new Color(180, 0, 0));
                        btn.setEnabled(false);
                    } else {
                        btn.setBackground(new Color(100, 100, 100));
                    }

                    btn.addActionListener(e -> {
                        if (btn.getBackground().equals(orangeColor)) {
                            btn.setBackground(new Color(100, 100, 100));
                            selectedChairs.remove(ghe.getMaGhe());
                        } else {
                            btn.setBackground(orangeColor);
                            selectedChairs.add(ghe.getMaGhe());
                        }
                    });

                    cell.add(btn);
                    centerPanel.add(cell);
                } else {
                    centerPanel.add(new JLabel(""));
                }
            }
        }

        centerPanel.add(new JLabel(""));

        for (int c = 1; c <= numCols; c++) {
            JPanel colWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            colWrapper.setOpaque(false);

            JLabel colLabel = new JLabel(String.format("%02d", c), SwingConstants.CENTER);
            colLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
            colLabel.setForeground(Color.WHITE);
            colLabel.setPreferredSize(new Dimension(35, 20));

            colWrapper.add(colLabel);
            centerPanel.add(colWrapper);
        }


        chairFrame.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        southPanel.setOpaque(false);

        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        JPanel legendPanel = new JPanel();
        legendPanel.setOpaque(false);
        addLegendItem(legendPanel, orangeColor, "Ghế đang chọn");
        addLegendItem(legendPanel, new Color(100, 100, 100), "Ghế còn trống");
        addLegendItem(legendPanel, new Color(180, 0, 0), "Ghế đã đặt");
        southPanel.add(legendPanel);

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.setBackground(orangeColor);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.setFocusPainted(false);
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnConfirm.setPreferredSize(new Dimension(150, 45));
        btnConfirm.addActionListener(e -> handleConfirmSelectChairs(selectedChairs, chairFrame, rap));

        JPanel btnWrapper = new JPanel();
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnConfirm);
        southPanel.add(btnWrapper);

        chairFrame.add(southPanel, BorderLayout.SOUTH);
        chairFrame.setVisible(true);
    }

    private void addLegendItem(JPanel panel, Color color, String text) {
        JPanel box = new JPanel();
        box.setBackground(color);
        box.setPreferredSize(new Dimension(25, 25));

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));

        panel.add(box);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(20));
    }
    private void hanldeSelectChair(ArrayList<String> selectedChairs, JButton btn) {
        if (selectedChairs.contains(btn.getText())) {
            selectedChairs.remove(btn.getText());
            btn.setBackground(Color.GREEN);
        } else {
            selectedChairs.add(btn.getText());
            btn.setBackground(Color.LIGHT_GRAY);
        }
    }

    private void handleConfirmSelectChairs(ArrayList<String> selectedChairs, JFrame chairFrame, Rap rap) {
        txtSoGhe.setText(String.join(", ", selectedChairs));
        btnDatVe.setEnabled(!selectedChairs.isEmpty());
        chairFrame.dispose();
        if (this.selectedChairs != null)
            this.selectedChairs.clear();
        this.selectedChairs = selectedChairs;
    }

    @Override
    public void loadData() {
        this.movieManager = new QuanLyPhim_DAO();
        this.suatChieuManager = new QuanLySuatChieu_DAO();
        this.rapManager = new QuanLyRap_DAO();
        this.chairManager = new QuanLyGhe_DAO();
        this.customerManager = new QuanLyKhachHang_DAO();
        LoadCBPhim();
    }

    private void LoadCBPhim() {
        this.movieList = this.movieManager.getAllPhim();
        if (this.cbPhim == null) {
            this.cbPhim = new JComboBox<>();
        } else {
            this.cbPhim.removeAllItems();
        }
        this.cbPhim.addItem("---Chọn phim---");
        for (Phim phim : this.movieList) {
            this.cbPhim.addItem(phim.getTenPhim());
        }
    }

    private void AutoFillCustomer() {
        String sdt = this.txtSDT.getText().trim();
        String regexSDT = "^(03|05|07|08|09)[0-9]{8}$";
        if (!sdt.matches(regexSDT)) {
            JOptionPane.showMessageDialog(this,
                    "số điện thoại phải có 10 số và 2 số đầu phải khớp với nhà mạng Việt Nam (03, 05, 07,...)",
                    "Lỗi cú pháp số điện thoại",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        KhachHang khachHang = this.customerManager.timKhachHangTheoSDT(sdt);
        if (khachHang == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng nào có số điện thoại: " + sdt,
                    "Hệ thống thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.txtHoTen.setText(khachHang.getHoTen());
        this.txtDiaChi.setText(khachHang.getDiaChi());
        this.cbGioiTinh.setSelectedItem(khachHang.getGioiTinh());
    }

    private boolean isGheDaDat(Ghe ghe, ArrayList<Ve> ticketList) {
        for (Ve ve : ticketList) {
            if (ve.getGhe().getMaGhe().equalsIgnoreCase(ghe.getMaGhe())) {
                return true;
            }
        }
        return false;
    }
}
