package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.QuanLyCTHD_DAO;
import dao.QuanLyGhe_DAO;
import dao.QuanLyHoaDon_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLyRap_DAO;
import dao.QuanLySuatChieu_DAO;
import dao.QuanLyVe_DAO;
import entity.ChiTietHoaDon;
import entity.Ghe;
import entity.HoaDon;
import entity.Phim;
import entity.Rap;
import entity.ResetForm;
import entity.SuatChieu;
import entity.Ve;
// iText 5
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class ThongTinVeModal extends JFrame {
    private QuanLySuatChieu_DAO suatChieuManager;
    private QuanLyPhim_DAO movieManager;
    private QuanLyRap_DAO rapManager;
    private QuanLyGhe_DAO chairManager;
    private QuanLyVe_DAO ticketManager;
    private QuanLyHoaDon_DAO billManager;
    private Font fChonGhe = new Font("Tahoma", Font.BOLD, 16);
    private JPanel parentform;
    private QuanLyCTHD_DAO cthdManager;
    private Dimension modalDimension = new Dimension(500, 600);
    private JButton btnInVe;
    private JButton btnHuy;
    private JButton btnThanhToan;
    private JButton btnXemHoaDon;
    private JLabel lblTrangThai;

    private HoaDon hoaDon;
    private Ve ticketOrigin;
    private SuatChieu suatChieu;
    private ArrayList<String> soGheDuocChon;
    private ArrayList<Ve> danhSachVeMoiThanhToan;

    public ThongTinVeModal(HoaDon hoaDon, Ve ve, JPanel parentForm, ArrayList<String> soGheDuocChon) {

        if (hoaDon == null || ve == null || parentForm == null)
            return;

        this.parentform = parentForm;
        this.hoaDon = hoaDon;
        this.ticketOrigin = ve;
        this.soGheDuocChon = soGheDuocChon;

        this.suatChieuManager = new QuanLySuatChieu_DAO();
        this.movieManager = new QuanLyPhim_DAO();
        this.rapManager = new QuanLyRap_DAO();
        this.chairManager = new QuanLyGhe_DAO();
        this.ticketManager = new QuanLyVe_DAO();
        this.billManager = new QuanLyHoaDon_DAO();
        this.cthdManager = new QuanLyCTHD_DAO();

        setSize(modalDimension);
        setLocationRelativeTo(this.parentform);
        setLayout(new BorderLayout());
        setTitle("Thông tin vé");

        JLabel lblTitle = new JLabel("THÔNG TIN VÉ XEM PHIM");
        Font fTitle = new Font("Tahoma", Font.BOLD, 20);
        lblTitle.setFont(fTitle);
        lblTitle.setForeground(Color.RED);
        JPanel pNorth = new JPanel();
        pNorth.add(lblTitle);

        add(pNorth, BorderLayout.NORTH);

        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBorder(BorderFactory.createTitledBorder("THÔNG TIN VÉ"));
        // mã vé, thời gian chiếu, tên phim, tên phòng chiếu, số ghế, số lượng vé, thời
        // gian đặt vé

        String maVe = this.ticketOrigin.getMaVe();
        this.suatChieu = this.suatChieuManager.timSuatChieu(this.ticketOrigin.getMaSuatChieu());
        if (suatChieu == null)
            return;
        Phim phim = this.movieManager.timPhimTheoMa(suatChieu.getMaPhim());
        String tenPhim = phim.getTenPhim();
        Rap rap = this.rapManager.findRapByID(suatChieu.getMaRap());
        String tenPhong = rap.getTenRap();
        String thoiGian = suatChieu.getGioChieu().toString() + ", "
                + suatChieu.getNgayChieu().toString();

//        String soVe = Integer.toString(this.hoaDon.getSoLuongVe());

        String soGhe = String.join(", ", this.soGheDuocChon);

        String thoiGianDatVe = ve.getNgayBan().toString();

        JLabel lblMaVe = new JLabel("  Mã vé:                   " + maVe);
        JLabel lblTenPhim = new JLabel("  Tên phim:             " + tenPhim);
        JLabel lblTenPhong = new JLabel("  Phòng chiếu:        " + tenPhong);
        JLabel lblThoiGian = new JLabel("  Thời gian:             " + thoiGian);
//        JLabel lblSoVe = new JLabel("  Số vé:                     " + soVe);
        JLabel lblSoGhe = new JLabel("  Số ghế:                   " + soGhe);
        JLabel lblThoiGianDatVe = new JLabel("  Thời gian đặt vé:  " + thoiGianDatVe);
        this.lblTrangThai = new JLabel("  Trạng thái:             " + this.ticketOrigin.getTrangThai());
        pCenter.add(Box.createVerticalStrut(10));
        pCenter.add(lblMaVe);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblTenPhim);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblTenPhong);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblThoiGian);
        pCenter.add(Box.createVerticalStrut(5));
//        pCenter.add(lblSoVe);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblSoGhe);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblThoiGianDatVe);
        pCenter.add(Box.createVerticalStrut(5));
        pCenter.add(lblTrangThai);
        pCenter.add(Box.createVerticalStrut(10));

        add(pCenter, BorderLayout.CENTER);

        JPanel pSouth = new JPanel();

        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(fChonGhe);
        btnThanhToan.setBackground(Color.GREEN);

        btnHuy = new JButton("Đóng");
        btnHuy.setFont(this.fChonGhe);
        btnHuy.setBackground(Color.RED);
        btnHuy.setForeground(Color.WHITE);

        btnInVe = new JButton("In vé");
        btnInVe.setFont(fChonGhe);
        btnInVe.setEnabled(false);

        btnXemHoaDon = new JButton("Xem hóa đơn");
        btnXemHoaDon.setFont(fChonGhe);
        btnXemHoaDon.setEnabled(false);

        if (this.ticketOrigin.isDaThanhToan()) {
            btnInVe.setEnabled(true);
            btnXemHoaDon.setEnabled(true);
            btnThanhToan.setEnabled(false);
        }

        pSouth.add(btnHuy);
        pSouth.add(btnThanhToan);
        pSouth.add(btnInVe);
        pSouth.add(btnXemHoaDon);

        add(pSouth, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> close());
        btnThanhToan.addActionListener(e -> thanhToan());
        btnInVe.addActionListener(e -> InVe());
        btnXemHoaDon.addActionListener(e -> xemHoaDon(this.hoaDon));

        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setVisible(true);
    }

    private void close() {
        this.dispose();
        ResetForm resetInterface = (ResetForm) this.parentform;
        if(resetInterface != null) resetInterface.resetForm();
        this.hoaDon = null;
    }

    private void InVe() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu vé PDF");
            chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            String defaultName = "Ve_" + this.ticketOrigin.getMaVe() + "_" + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            chooser.setSelectedFile(new File(defaultName));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
                return;
            File out = chooser.getSelectedFile();
            if (!out.getName().toLowerCase().endsWith(".pdf")) {
                out = new File(out.getParentFile(), out.getName() + ".pdf");
            }

            // gather data
            Phim phim = null;
            Rap rap = null;
            if (this.suatChieu != null) {
                phim = this.movieManager.timPhimTheoMa(this.suatChieu.getMaPhim());
                rap = this.rapManager.findRapByID(this.suatChieu.getMaRap());
            }

            String tenPhim = phim == null ? "" : phim.getTenPhim();
            String tenPhong = rap == null ? "" : rap.getTenRap();
            String thoiGian = this.suatChieu == null ? "" : this.suatChieu.getGioChieu().toString() + ", "
                    + this.suatChieu.getNgayChieu().toString();
//            String soVe = Integer.toString(this.hoaDon.getSoLuongVe());
            String soGhe = String.join(", ", this.soGheDuocChon);
            String thoiGianDatVe = this.ticketOrigin.getNgayBan().toString();
            String trangThai = this.ticketOrigin.getTrangThai();

            // create PDF with embedded Unicode font (if available)
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(out));
            doc.open();

            // try to find a Unicode TTF in project or Windows fonts
            String fontPath = null;
            String[] candidates = new String[] { "fonts/Unicode8.ttf", "fonts/arialuni.ttf",
                    "C:/Windows/Fonts/ARIALUNI.TTF", "C:/Windows/Fonts/ARIAL.TTF" };
            for (String p : candidates) {
                if (new File(p).exists()) {
                    fontPath = p;
                    break;
                }
            }

            BaseFont bf;
            if (fontPath != null) {
                bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } else {
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
            }

            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

            Paragraph title = new Paragraph("VÉ XEM PHIM", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" ", fontNormal));

            doc.add(new Paragraph("Mã vé: " + this.ticketOrigin.getMaVe(), fontNormal));
            doc.add(new Paragraph("Tên phim: " + tenPhim, fontNormal));
            doc.add(new Paragraph("Phòng chiếu: " + tenPhong, fontNormal));
            doc.add(new Paragraph("Thời gian: " + thoiGian, fontNormal));
//            doc.add(new Paragraph("Số vé: " + soVe, fontNormal));
            doc.add(new Paragraph("Số ghế: " + soGhe, fontNormal));
            doc.add(new Paragraph("Thời gian đặt vé: " + thoiGianDatVe, fontNormal));
            doc.add(new Paragraph("Trạng thái: " + trangThai, fontNormal));

            doc.close();

            JOptionPane.showMessageDialog(this, "Đã lưu vé PDF: " + out.getAbsolutePath(), "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void thanhToan() {
        if (this.ticketOrigin.isDaThanhToan()) {
            JOptionPane.showMessageDialog(this, "Vé đã được thanh toán!");
            return;
        }

        this.danhSachVeMoiThanhToan = new ArrayList<>();
        for (String tenGhe : this.soGheDuocChon) {
            Ghe ghe = this.chairManager.TimGheTheoTen(tenGhe, this.suatChieu.getMaRap());
            Ve veMoi = xuLyTaoVeTheoGhe(ghe);
            if (veMoi != null) {
                this.danhSachVeMoiThanhToan.add(veMoi);
                this.ticketManager.add(veMoi);
            }
        }

        this.billManager.add(this.hoaDon);
        xuLyTaoChiTietHoaDon(this.hoaDon, this.danhSachVeMoiThanhToan, this.suatChieu.getGiaVe());

        this.ticketOrigin.setDaThanhToan(true);
        this.lblTrangThai.setText("  Trạng thái:             Đã thanh toán");

        this.btnThanhToan.setEnabled(false);
        this.btnInVe.setEnabled(true);
        this.btnXemHoaDon.setEnabled(true);

        JOptionPane.showMessageDialog(this, "Thanh toán thành công! Bạn có thể xem hóa đơn ngay bây giờ.");
    }
    private Ve xuLyTaoVeTheoGhe(Ghe ghe) {
        if (ghe == null) {
            return null;
        }
        Ve ve = createTicket();
        if (ve == null) {
            return null;
        }
        ve.setDaThanhToan(true);
        ve.setGhe(ghe);
        return ve;
    }

    private Ve createTicket() {
        if (this.suatChieu == null)
            return null;
        Ve ve = new Ve(QuanLyVe_DAO.taoMaVeTuDong());
        ve.setMaSuatChieu(this.suatChieu.getMaSuatChieu());
        ve.setNgayBan(LocalDate.now());
        ve.setDaThanhToan(false);
        return ve;
    }

    private void xemHoaDon(HoaDon hoaDon) {
        if (this.hoaDon == null) return;
        // Đảm bảo danhSachVeMoiThanhToan đã được khởi tạo trong hàm thanhToan()
        new HoaDonModal(this.hoaDon, this.parentform, this.danhSachVeMoiThanhToan);
    }

    private void xuLyTaoChiTietHoaDon(HoaDon hoaDon, ArrayList<Ve> danhSachVeDaDat, double giaVe) {
        for (Ve ve : danhSachVeDaDat) {
            ChiTietHoaDon cthd = new ChiTietHoaDon(hoaDon, ve, 1, giaVe);
            this.cthdManager.add(cthd);
        }
    }
}
