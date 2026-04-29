package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.QuanLyCTHD_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Phim;
import entity.SuatChieu;
import entity.Ve;
// iText 5
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

public class HoaDonModal extends JFrame {
    private QuanLyCTHD_DAO cthdManager;
    private QuanLySuatChieu_DAO suatChieuManager;
    private QuanLyPhim_DAO movieManager;
    private HoaDon hoaDon;

    public HoaDonModal(HoaDon hoaDon, JPanel pOwner) {
        this.hoaDon = hoaDon;
        this.suatChieuManager = new QuanLySuatChieu_DAO();
        this.movieManager = new QuanLyPhim_DAO();
        this.cthdManager = new QuanLyCTHD_DAO();

        setSize(800, 700);
        setLocationRelativeTo(pOwner);
        setLayout(new BorderLayout());

        JPanel pNorth = new JPanel(new BorderLayout());
        JLabel lblHoaDon = new JLabel("HÓA ĐƠN BÁN VÉ");
        Font fTitleHoaDon = new Font("Arial", Font.BOLD, 20);
        lblHoaDon.setFont(fTitleHoaDon);
        lblHoaDon.setForeground(Color.RED);
        JPanel pTitle = new JPanel();
        pTitle.add(lblHoaDon);
        pNorth.add(pTitle, BorderLayout.NORTH);

        if (hoaDon == null || cthdManager == null) {
            JOptionPane.showMessageDialog(this, "Không có thông tin về hóa đơn này", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel pHoaDon = new JPanel();
        pHoaDon.setLayout(new BoxLayout(pHoaDon, BoxLayout.Y_AXIS));

        JLabel lblMaHD = new JLabel("   Mã hóa đơn:             " + hoaDon.getMaHoaDon());
        JLabel lblNgayLap = new JLabel("   Ngày lập hóa đơn:   " + hoaDon.getNgayLap().toString());
        JLabel lblSoLuongVe = new JLabel("   Số lượng vé:             " + hoaDon.getSoLuongVe());
        JLabel lblTongTien = new JLabel("   Tổng tiền:                  " + hoaDon.getTongTien());

        JLabel lblNhanVien = new JLabel("   Nhân viên bán vé:  " + hoaDon.getNhanVien().getTenNV());
        JLabel lblSoDienThoai = new JLabel("   Số liên hệ:               " + hoaDon.getNhanVien().getSoDienThoai());
        JLabel lblDiaChiRap = new JLabel("   Địa chỉ rạp chiếu:  " +
                "12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP. Hồ Chí Minh");
        JLabel lblEmail = new JLabel("   Email:                       " + "3TCinema@gmail.com");

        JLabel lblTenKhachHang = new JLabel("   Khách hàng:        " + hoaDon.getKhachHang().getHoTen());
        JLabel lblDiaChiKH = new JLabel("   Địa chỉ:                 " + hoaDon.getKhachHang().getDiaChi());
        JLabel lblSoDienThoaiKH = new JLabel("   Số điện thoại:      " + hoaDon.getKhachHang().getSoDT());

        JPanel pThongTinNhanVien = new JPanel();
        pThongTinNhanVien.setLayout(new BoxLayout(pThongTinNhanVien, BoxLayout.Y_AXIS));
        pThongTinNhanVien.setBorder(BorderFactory.createTitledBorder("THÔNG TIN NHÀ CUNG CẤP DỊCH VỤ"));
        pThongTinNhanVien.add(Box.createVerticalStrut(5));
        pThongTinNhanVien.add(lblNhanVien);
        pThongTinNhanVien.add(Box.createVerticalStrut(5));
        pThongTinNhanVien.add(lblSoDienThoai);
        pThongTinNhanVien.add(Box.createVerticalStrut(5));
        pThongTinNhanVien.add(lblDiaChiRap);
        pThongTinNhanVien.add(Box.createVerticalStrut(5));
        pThongTinNhanVien.add(lblEmail);
        pThongTinNhanVien.add(Box.createVerticalStrut(5));

        JPanel pThongTinKhachHang = new JPanel();
        pThongTinKhachHang.setLayout(new BoxLayout(pThongTinKhachHang, BoxLayout.Y_AXIS));
        pThongTinKhachHang.setBorder(BorderFactory.createTitledBorder("THÔNG TIN KHÁCH HÀNG"));
        pThongTinKhachHang.add(Box.createVerticalStrut(5));
        pThongTinKhachHang.add(lblTenKhachHang);
        pThongTinKhachHang.add(Box.createVerticalStrut(5));
        pThongTinKhachHang.add(lblSoDienThoaiKH);
        pThongTinKhachHang.add(Box.createVerticalStrut(5));
        pThongTinKhachHang.add(lblDiaChiKH);
        pThongTinKhachHang.add(Box.createVerticalStrut(5));

        JPanel pThongTinHoaDon = new JPanel();
        pThongTinHoaDon.setLayout(new BoxLayout(pThongTinHoaDon, BoxLayout.Y_AXIS));
        pThongTinHoaDon.setBorder(BorderFactory.createTitledBorder("THÔNG TIN HÓA ĐƠN"));
        pThongTinHoaDon.add(Box.createVerticalStrut(5));
        pThongTinHoaDon.add(lblMaHD);
        pThongTinHoaDon.add(Box.createVerticalStrut(5));
        pThongTinHoaDon.add(lblNgayLap);
        pThongTinHoaDon.add(Box.createVerticalStrut(5));
        pThongTinHoaDon.add(lblSoLuongVe);
        pThongTinHoaDon.add(Box.createVerticalStrut(5));
        pThongTinHoaDon.add(lblTongTien);
        pThongTinHoaDon.add(Box.createVerticalStrut(5));

        pHoaDon.add(pThongTinNhanVien);
        pHoaDon.add(pThongTinKhachHang);
        pHoaDon.add(pThongTinHoaDon);

        pNorth.add(pHoaDon, BorderLayout.CENTER);
        add(pNorth, BorderLayout.NORTH);

        ArrayList<ChiTietHoaDon> cthdList = cthdManager.timCTHDTheoMaHoaDon(hoaDon.getMaHoaDon());
        Object[] columns = { "STT", "Mã vé", "Tên phim", "Số lượng", "Giá vé", "Thành tiền" };
        DefaultTableModel model = new DefaultTableModel(null, columns);
        int stt = 1;
        for (ChiTietHoaDon cthd : cthdList) {
            Ve ve = cthd.getVe();
            // Tìm phim dựa vào suất chiếu
            SuatChieu suatChieu = suatChieuManager.timSuatChieu(ve.getMaSuatChieu());
            if (suatChieu != null) {
                Phim phim = movieManager.timPhimTheoMa(suatChieu.getMaPhim());

                Object[] data = { stt, ve.getMaVe(), phim.getTenPhim(), cthd.getSoLuong(), cthd.getGiaVe(),
                        cthd.tinhThanhTien() };
                model.addRow(data);
            }
        }
        JTable table = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(table);
        JPanel pCenter = new JPanel(new BorderLayout());
        JPanel pCen_title = new JPanel();
        JLabel lblChiTietHoaDon = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblChiTietHoaDon.setFont(new Font("Arial", Font.BOLD, 18));
        pCen_title.add(lblChiTietHoaDon);
        pCenter.add(pCen_title, BorderLayout.NORTH);
        pCenter.add(scrollTable, BorderLayout.CENTER);
        JPanel pSouth = new JPanel();
        Font fButton = new Font("Arial", Font.BOLD, 16);

        JButton btnDone = new JButton("Xong");
        btnDone.setFont(fButton);
        btnDone.setBackground(Color.green);
        JButton btnPrint = new JButton("In hóa đơn");
        btnPrint.setFont(fButton);
        pSouth.add(btnPrint);
        pSouth.add(btnDone);

        btnDone.addActionListener(e -> close());
    btnPrint.addActionListener(e -> handleInHoaDon());

        add(pNorth, BorderLayout.NORTH);
        add(pCenter, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);

        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setVisible(true);
    }

    private void handleInHoaDon() {
        if (this.hoaDon == null) {
            JOptionPane.showMessageDialog(this, "Không có thông tin hóa đơn để in", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu hóa đơn PDF");
            chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            String defaultName = "HoaDon_" + this.hoaDon.getMaHoaDon() + "_"
                    + java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".pdf";
            chooser.setSelectedFile(new File(defaultName));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
                return;
            File out = chooser.getSelectedFile();
            if (!out.getName().toLowerCase().endsWith(".pdf")) {
                out = new File(out.getParentFile(), out.getName() + ".pdf");
            }

            // prepare data
            ArrayList<ChiTietHoaDon> cthdList = cthdManager.timCTHDTheoMaHoaDon(this.hoaDon.getMaHoaDon());

            NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));
            nf.setMinimumFractionDigits(0);

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(out));
            doc.open();

            // Embed a Unicode-capable TTF when available so Vietnamese characters render correctly
            String fontPath = null;
            String[] candidates = new String[] { "fonts/Unicode8.ttf", "fonts/arialuni.ttf",
                    "C:/Windows/Fonts/ARIALUNI.TTF", "C:/Windows/Fonts/ARIAL.TTF", "C:/Windows/Fonts/times.ttf" };
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
                // fallback: use a standard font (may not render Vietnamese accents correctly)
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
            }

            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font fontHeader = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN VÉ", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" ", fontNormal));

            doc.add(new Paragraph("Mã hóa đơn: " + this.hoaDon.getMaHoaDon(), fontNormal));
            doc.add(new Paragraph("Ngày lập hóa đơn: " + this.hoaDon.getNgayLap().toString(), fontNormal));
            doc.add(new Paragraph("Số lượng vé: " + this.hoaDon.getSoLuongVe(), fontNormal));
            doc.add(new Paragraph("Tổng tiền: " + nf.format(this.hoaDon.getTongTien()), fontNormal));
            doc.add(new Paragraph(" ", fontNormal));

            doc.add(new Paragraph("Nhân viên bán vé: " + this.hoaDon.getNhanVien().getTenNV(), fontNormal));
            doc.add(new Paragraph("Số liên hệ: " + this.hoaDon.getNhanVien().getSoDienThoai(), fontNormal));
            doc.add(new Paragraph("Địa chỉ rạp chiếu: 12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP. Hồ Chí Minh",
                    fontNormal));
            doc.add(new Paragraph("Email: 3TCinema@gmail.com", fontNormal));
            doc.add(new Paragraph(" ", fontNormal));

            doc.add(new Paragraph("Khách hàng: " + this.hoaDon.getKhachHang().getHoTen(), fontNormal));
            doc.add(new Paragraph("Địa chỉ: " + this.hoaDon.getKhachHang().getDiaChi(), fontNormal));
            doc.add(new Paragraph("Số điện thoại: " + this.hoaDon.getKhachHang().getSoDT(), fontNormal));
            doc.add(new Paragraph(" ", fontNormal));

            // table of details
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            String[] headers = { "STT", "Mã vé", "Tên phim", "Số lượng", "Giá vé", "Thành tiền" };
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(h, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int stt = 1;
            for (ChiTietHoaDon cthd : cthdList) {
                Ve ve = cthd.getVe();
                SuatChieu suatChieu = suatChieuManager.timSuatChieu(ve.getMaSuatChieu());
                String tenPhim = "";
                if (suatChieu != null) {
                    Phim phim = movieManager.timPhimTheoMa(suatChieu.getMaPhim());
                    if (phim != null)
                        tenPhim = phim.getTenPhim();
                }

                table.addCell(new PdfPCell(new Paragraph(Integer.toString(stt), fontNormal)));
                table.addCell(new PdfPCell(new Paragraph(ve.getMaVe(), fontNormal)));
                table.addCell(new PdfPCell(new Paragraph(tenPhim, fontNormal)));
                table.addCell(new PdfPCell(new Paragraph(Integer.toString(cthd.getSoLuong()), fontNormal)));
                table.addCell(new PdfPCell(new Paragraph(nf.format(cthd.getGiaVe()), fontNormal)));
                table.addCell(new PdfPCell(new Paragraph(nf.format(cthd.tinhThanhTien()), fontNormal)));
                stt++;
            }

            doc.add(table);

            // summary
            doc.add(new Paragraph(" ", fontNormal));
            doc.add(new Paragraph("Tổng: " + nf.format(this.hoaDon.getTongTien()), fontNormal));

            doc.close();

            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn PDF: " + out.getAbsolutePath(), "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void close() {
        this.dispose();
        return;
    }

}
