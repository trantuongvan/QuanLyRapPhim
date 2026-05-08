package gui;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import dao.QuanLyCTHD_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Phim;
import entity.SuatChieu;
import entity.Ve;

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
    private DefaultTableModel model;
    private JTable table;

    public HoaDonModal(HoaDon hd, JPanel parent, List<Ve> listVe) {
        this.hoaDon = hd;
        this.cthdManager = new QuanLyCTHD_DAO();
        this.suatChieuManager = new QuanLySuatChieu_DAO();
        this.movieManager = new QuanLyPhim_DAO();

        setTitle("Chi tiết hóa đơn");
        setSize(850, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pNorth = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("HÓA ĐƠN BÁN VÉ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitle.setForeground(Color.RED);
        pNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pInfoContainer = new JPanel();
        pInfoContainer.setLayout(new BoxLayout(pInfoContainer, BoxLayout.Y_AXIS));
        pInfoContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pProvider = createInfoPanel("THÔNG TIN NHÀ CUNG CẤP");
        pProvider.add(new JLabel("Nhân viên: " + hoaDon.getNhanVien().getTenNV()));
        pProvider.add(new JLabel("Địa chỉ: 12 Nguyễn Văn Bảo, Q. Gò Vấp, TP. HCM"));

        JPanel pCustomer = createInfoPanel("THÔNG TIN KHÁCH HÀNG");
        pCustomer.add(new JLabel("Khách hàng: " + hoaDon.getKhachHang().getHoTen()));
        pCustomer.add(new JLabel("Số điện thoại: " + hoaDon.getKhachHang().getSoDT()));

        JPanel pInvoice = createInfoPanel("THÔNG TIN GIAO DỊCH");
        pInvoice.add(new JLabel("Mã hóa đơn: " + hoaDon.getMaHoaDon()));
        pInvoice.add(new JLabel("Ngày lập: " + hoaDon.getNgayLap().toString()));

        pInfoContainer.add(pProvider);
        pInfoContainer.add(pCustomer);
        pInfoContainer.add(pInvoice);
        pNorth.add(pInfoContainer, BorderLayout.CENTER);

        String[] columns = { "STT", "Mã vé", "Tên phim", "Số lượng", "Giá vé", "Thành tiền" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        loadTableData(listVe);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createTitledBorder("DANH SÁCH VÉ"));

        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrint = new JButton("In hóa đơn (PDF)");
        JButton btnDone = new JButton("Đóng");

        pSouth.add(btnPrint);
        pSouth.add(btnDone);

        add(pNorth, BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);

        btnDone.addActionListener(e -> dispose());
        btnPrint.addActionListener(e -> handleInHoaDon());

        setVisible(true);
    }

    private void loadTableData(List<Ve> listVe) {
        model.setRowCount(0);
        int stt = 1;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        if (listVe != null && !listVe.isEmpty()) {
            for (Ve ve : listVe) {
                SuatChieu sc = suatChieuManager.timSuatChieu(ve.getMaSuatChieu());
                Phim p = (sc != null) ? movieManager.timPhimTheoMa(sc.getMaPhim()) : null;

                model.addRow(new Object[]{
                        stt++,
                        ve.getMaVe(),
                        (p != null) ? p.getTenPhim() : "N/A",
                        1,
                        sc.getGiaVe(),
                        sc.getGiaVe()
                });
            }
        }
        else {
            ArrayList<ChiTietHoaDon> dbList = cthdManager.timCTHDTheoMaHoaDon(hoaDon.getMaHoaDon());
            for (ChiTietHoaDon cthd : dbList) {
                SuatChieu sc = suatChieuManager.timSuatChieu(cthd.getVe().getMaSuatChieu());
                Phim p = (sc != null) ? movieManager.timPhimTheoMa(sc.getMaPhim()) : null;

                model.addRow(new Object[]{
                        stt++,
                        cthd.getVe().getMaVe(),
                        (p != null) ? p.getTenPhim() : "N/A",
                        cthd.getSoLuong(),
                        cthd.getGiaVe(),
                        cthd.tinhThanhTien()
                });
            }
        }
    }

    private JPanel createInfoPanel(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
    }

    private void handleInHoaDon() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(".pdf")) file = new File(file.getAbsolutePath() + ".pdf");

            try {
                Document doc = new Document();
                PdfWriter.getInstance(doc, new FileOutputStream(file));
                doc.open();

                BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

                Paragraph title = new Paragraph("HÓA ĐƠN BÁN VÉ", fontTitle);
                title.setAlignment(Element.ALIGN_CENTER);
                doc.add(title);
                doc.add(new Paragraph("\n"));

                doc.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHoaDon(), fontNormal));
                doc.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getHoTen(), fontNormal));
                doc.add(new Paragraph("Nhân viên: " + hoaDon.getNhanVien().getTenNV(), fontNormal));
                doc.add(new Paragraph("\n"));

                PdfPTable pdfTable = new PdfPTable(6);
                pdfTable.setWidthPercentage(100);
                String[] headers = {"STT", "Mã vé", "Phim", "SL", "Giá", "T.Tiền"};

                for (String h : headers) {
                    pdfTable.addCell(new PdfPCell(new Paragraph(h, fontBold)));
                }

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < 6; j++) {
                        pdfTable.addCell(new PdfPCell(new Paragraph(model.getValueAt(i, j).toString(), fontNormal)));
                    }
                }

                doc.add(pdfTable);
                doc.add(new Paragraph("\nTổng tiền: " + hoaDon.getTongTien() + " VND", fontBold));

                doc.close();
                JOptionPane.showMessageDialog(this, "Xuất PDF thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
}