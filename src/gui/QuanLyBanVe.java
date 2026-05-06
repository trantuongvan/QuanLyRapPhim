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
import java.awt.Dialog.ModalExclusionType;
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

    public QuanLyBanVe() {
        setLayout(new BorderLayout(10, 10));
        // Load database
        loadData();
        // ===== NORTH: Tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ BÁN VÉ", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.RED);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));

        this.cbPhong = new JComboBox<>(new String[] { "---Chọn phòng---" });
        this.cbSuatChieu = new JComboBox<>(new String[] { "---Chọn suất chiếu---" });
        this.cbPhong.setEnabled(false);
        this.cbSuatChieu.setEnabled(false);

        JPanel pChonPhim = new JPanel();
        pChonPhim.setLayout(new BoxLayout(pChonPhim, BoxLayout.Y_AXIS));
        pChonPhim.setBorder(BorderFactory.createTitledBorder("CHỌN VÉ XEM PHIM"));

        JPanel pPhim = new JPanel();
        pPhim.setLayout(new BoxLayout(pPhim, BoxLayout.X_AXIS));
        pPhim.add(new JLabel("     Chọn phim:              "));
        pPhim.add(this.cbPhim);
        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pPhim);

        JPanel pPhong = new JPanel();
        pPhong.setLayout(new BoxLayout(pPhong, BoxLayout.X_AXIS));
        pPhong.add(new JLabel("     Chọn phòng:            "));
        pPhong.add(this.cbPhong);
        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pPhong);

        JPanel pSuat = new JPanel();
        pSuat.setLayout(new BoxLayout(pSuat, BoxLayout.X_AXIS));
        pSuat.add(new JLabel("     Chọn suất chiếu:    "));
        pSuat.add(cbSuatChieu);
        this.fChonGhe = new Font("Arial", Font.BOLD, 16);
        btnChonGhe = new JButton("Chọn ghế");
        btnChonGhe.setEnabled(false);
        btnChonGhe.setBackground(Color.RED);
        btnChonGhe.setForeground(Color.WHITE);
        btnChonGhe.setFont(fChonGhe);

        pSuat.add(Box.createHorizontalStrut(5));
        pSuat.add(btnChonGhe);
        pSuat.add(Box.createHorizontalStrut(30));

        pChonPhim.add(Box.createVerticalStrut(20));
        pChonPhim.add(pSuat);
        pChonPhim.add(Box.createVerticalStrut(20));

        pCenter.add(pChonPhim);

        txtHoTen = new JTextField();

        txtDiaChi = new JTextField();

        txtSDT = new JTextField();

        cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });

        txtSoGhe = new JTextField();
        txtSoGhe.setEditable(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("THÔNG TIN KHÁCH HÀNG"));

        JPanel pRow1 = new JPanel();
        pRow1.setLayout(new BoxLayout(pRow1, BoxLayout.X_AXIS));
        pRow1.add(new JLabel("     Họ tên:               "));
        pRow1.add(txtHoTen);

        JPanel pRow2 = new JPanel();
        pRow2.setLayout(new BoxLayout(pRow2, BoxLayout.X_AXIS));
        pRow2.add(new JLabel("     Số điện thoại:   "));
        pRow2.add(txtSDT);

        JPanel pRow3 = new JPanel();
        pRow3.setLayout(new BoxLayout(pRow3, BoxLayout.X_AXIS));
        pRow3.add(new JLabel("     Địa chỉ:              "));
        pRow3.add(txtDiaChi);

        JPanel pRow4 = new JPanel();
        pRow4.setLayout(new BoxLayout(pRow4, BoxLayout.X_AXIS));
        pRow4.add(new JLabel("     Giới tính:            "));
        pRow4.add(cbGioiTinh);

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(pRow1);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(pRow2);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(pRow3);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(pRow4);
        infoPanel.add(Box.createVerticalStrut(20));

        txtTenPhim = new JTextField();
        txtTenPhim.setEditable(false);

        txtTenPhong = new JTextField();
        txtTenPhong.setEditable(false);

        txtThoiLuong = new JTextField();
        txtThoiLuong.setEditable(false);

        txtTheLoai = new JTextField();
        txtTheLoai.setEditable(false);

        txtSuatChieu = new JTextField();
        txtSuatChieu.setEditable(false);
        JPanel pInfoMovie = new JPanel();
        pInfoMovie.setLayout(new BoxLayout(pInfoMovie, BoxLayout.Y_AXIS));
        pInfoMovie.setBorder(BorderFactory.createTitledBorder("THÔNG TIN VÉ XEM PHIM"));

        JPanel pTenPhim = new JPanel();
        pTenPhim.setLayout(new BoxLayout(pTenPhim, BoxLayout.X_AXIS));
        pTenPhim.add(new JLabel("     Tên phim:      "));
        pTenPhim.add(txtTenPhim);

        JPanel pTenPhong = new JPanel();
        pTenPhong.setLayout(new BoxLayout(pTenPhong, BoxLayout.X_AXIS));
        pTenPhong.add(new JLabel("     Phòng:           "));
        pTenPhong.add(txtTenPhong);

        JPanel pThoiLuong = new JPanel();
        pThoiLuong.setLayout(new BoxLayout(pThoiLuong, BoxLayout.X_AXIS));
        pThoiLuong.add(new JLabel("     Thời lượng:  "));
        pThoiLuong.add(txtThoiLuong);

        JPanel pTheLoai = new JPanel();
        pTheLoai.setLayout(new BoxLayout(pTheLoai, BoxLayout.X_AXIS));
        pTheLoai.add(new JLabel("     Thể loại:         "));
        pTheLoai.add(txtTheLoai);

        JPanel pSuatChieu = new JPanel();
        pSuatChieu.setLayout(new BoxLayout(pSuatChieu, BoxLayout.X_AXIS));
        pSuatChieu.add(new JLabel("     Suất chiếu:    "));
        pSuatChieu.add(txtSuatChieu);

        JPanel pSoGhe = new JPanel();
        pSoGhe.setLayout(new BoxLayout(pSoGhe, BoxLayout.X_AXIS));
        pSoGhe.add(new JLabel("     Ghế:                 "));
        pSoGhe.add(txtSoGhe);

        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pTenPhim);
        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pTenPhong);
        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pThoiLuong);
        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pTheLoai);
        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pSuatChieu);
        pInfoMovie.add(Box.createVerticalStrut(20));
        pInfoMovie.add(pSoGhe);
        pInfoMovie.add(Box.createVerticalStrut(20));

        JPanel pInfoBorder = new JPanel(new BorderLayout());
        pInfoBorder.add(infoPanel, BorderLayout.NORTH);
        pInfoBorder.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        pInfoBorder.add(pInfoMovie, BorderLayout.SOUTH);

        pCenter.add(pInfoBorder);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnXoaChon = new JButton("Xóa lựa chọn");
        btnXoaChon.setFont(fChonGhe);
        btnDatVe = new JButton("Xác nhận đặt vé");
        btnDatVe.setBackground(Color.green);
        btnDatVe.setEnabled(false);
        btnDatVe.setFont(fChonGhe);
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
        String regexHoten = "^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\\s[A-ZÀ-Ỹ][a-zà-ỹ]+)+$"; // Gồm Họ và tên - chữ cái đầu phải viết hoa
        String regexSDT = "^(03|05|07|08|09)[0-9]{8}$"; // số điện thoại - có 10 số
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
        // lưu khách hàng
        customerManager.add(khachHang);

        // chuyển danh sách ghế đã chọn sang tình trạng đã đặt
        if (this.selectedChairs == null || this.suatChieuDuocChon == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn ghế !", "Lỗi đặt vé", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Ve ve = createTicket();
        float giaVe = this.suatChieuDuocChon.getGiaVe();
        HoaDon hoaDon = xuLyTaoHoaDon(khachHang, this.selectedChairs.size(), giaVe);

        // Show thong tin ve
        new ThongTinVeModal(hoaDon, ve, this, this.selectedChairs);
    }

    private HoaDon xuLyTaoHoaDon(KhachHang khachHang, int soLuongVe, float giaVe) {
        // Get NhanVien đang đăng nhập vào hệ thống - giả sử có mã là NV01
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
        // Mở khóa bước chọn phòng
        UnlockChooseRoom();
    }

    private void UnlockChooseRoom() {
        ArrayList<SuatChieu> dsSuatChieu = suatChieuManager.getSuatChieuTheoPhim(this.suatChieuDuocChon.getMaPhim());
        cbPhong.setEnabled(true);
        cbPhong.removeAllItems();
        cbPhong.addItem("---Chọn phòng---");
        // lưu danh sách mã rạp
        roomIDSelectList = new ArrayList<>();
        roomIDSelectList.add("");
        // Lấy mã phòng không trùng lặp
        HashSet<String> dsMaRap = new HashSet<>();
        for (SuatChieu suatChieu : dsSuatChieu) {
            dsMaRap.add(suatChieu.getMaRap());
        }
        for (String maRap : dsMaRap) {
            Rap rap = rapManager.findRapByID(maRap);
            if (rap != null) {
                // lưu danh sách tên rạp
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
        // Lưu danh sách mã suất chiếu
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
        // Tới phần chọn ghế
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
        chairFrame.getContentPane().setBackground(new Color(10, 10, 10)); // Dark background
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
                        if (btn.getBackground().equals(Color.ORANGE)) {
                            btn.setBackground(new Color(100, 100, 100));
                            selectedChairs.remove(ghe.getMaGhe());
                        } else {
                            btn.setBackground(Color.ORANGE);
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
        addLegendItem(legendPanel, Color.ORANGE, "Ghế đang chọn");
        addLegendItem(legendPanel, new Color(100, 100, 100), "Ghế còn trống");
        addLegendItem(legendPanel, new Color(180, 0, 0), "Ghế đã đặt");
        southPanel.add(legendPanel);

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.setBackground(Color.ORANGE);
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
        String regexSDT = "^(03|05|07|08|09)[0-9]{8}$"; // số điện thoại - có 10 số
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

    // Tìm trong danh sách vé có tồn tại mã ghế này không
    private boolean isGheDaDat(Ghe ghe, ArrayList<Ve> ticketList) {
        for (Ve ve : ticketList) {
            if (ve.getGhe().getMaGhe().equalsIgnoreCase(ghe.getMaGhe())) {
                return true;
            }
        }
        return false;
    }
}
