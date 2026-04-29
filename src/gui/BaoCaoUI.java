package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.QuanLyHoaDon_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import entity.Phim;
import entity.SuatChieu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;

// iText 5
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BaoCaoUI extends JFrame {

    private DefaultTableModel model;
    private QuanLyPhim_DAO quanLyPhim_Dao;
    private QuanLyHoaDon_DAO quanLyHoaDon_DAO;
    private QuanLySuatChieu_DAO quanLySuatChieu_DAO;

    private String doanhThu;
    private double totalDoanhThu;
    private int totalPhim;
    private int totalVe;

    public BaoCaoUI(String thang) {
        quanLyPhim_Dao = new QuanLyPhim_DAO();
        quanLyHoaDon_DAO = new QuanLyHoaDon_DAO();
        quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        setTitle("Báo Cáo");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel pNorth = new JPanel(new BorderLayout());
        JLabel lblTieuDe = new JLabel("BÁO CÁO", SwingConstants.CENTER);
        lblTieuDe.setForeground(new Color(220, 20, 60));

        lblTieuDe.setFont(lblTieuDe.getFont().deriveFont(24.0f));
        pNorth.add(lblTieuDe, BorderLayout.NORTH);
        tinhTongThongKe(thang);
        JPanel pNoidung = new JPanel(new GridLayout(4, 1));
        JLabel lblNgayBaoCao = new JLabel(
                "Ngày Báo cáo    : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblNgayBaoCao.setHorizontalAlignment(SwingConstants.CENTER);
        lblNgayBaoCao.setFont(lblNgayBaoCao.getFont().deriveFont(16.0f));
        pNoidung.add(lblNgayBaoCao);

        JLabel lblTongSoPhim = new JLabel("Tổng số Phim    : " + totalPhim);
        lblTongSoPhim.setHorizontalAlignment(SwingConstants.CENTER);
        lblTongSoPhim.setFont(lblTongSoPhim.getFont().deriveFont(16.0f));
        pNoidung.add(lblTongSoPhim);

        JLabel lblTongSoVe = new JLabel("Tổng số vé        : " + totalVe);
        lblTongSoVe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTongSoVe.setFont(lblTongSoVe.getFont().deriveFont(16.0f));
        pNoidung.add(lblTongSoVe);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        doanhThu = nf.format(totalDoanhThu);

        JLabel lblTongDoanhThu = new JLabel("Tổng doanh thu : " + doanhThu);
        lblTongDoanhThu.setHorizontalAlignment(SwingConstants.CENTER);
        lblTongDoanhThu.setFont(lblTongDoanhThu.getFont().deriveFont(16.0f));
        pNoidung.add(lblTongDoanhThu);
        pNorth.add(pNoidung, BorderLayout.CENTER);
        add(pNorth, BorderLayout.NORTH);

        JPanel pCenter = new JPanel(new BorderLayout());
        JTable table = new JTable();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[] { "Mã phim", "Tên Phim", "Số Lượng Vé", "Doanh Thu" });
        table.setModel(model);
        capNhatBang(thang);
        JScrollPane scrollPane = new JScrollPane(table);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);

        JPanel pSouth = new JPanel();
        JButton btnDong = new JButton("Đóng");
        JButton inBaoCao = new JButton("In Báo Cáo");
        btnDong.addActionListener(e -> close());
        inBaoCao.addActionListener(e -> handleInHoaDon());
        pSouth.add(btnDong);
        pSouth.add(inBaoCao);
        add(pSouth, BorderLayout.SOUTH);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setVisible(true);
    }

    private void tinhTongThongKe(String thang) {
        totalDoanhThu = 0;
        totalPhim = 0;
        totalVe = 0;

        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {
            if (ktPhimTrongThang(phim.getMaPhim(), Integer.parseInt(thang))) {
                totalPhim++;
                int soLuongVe = quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim());
                totalVe += soLuongVe;
                totalDoanhThu += quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim());
            }
        }
    }

    private void capNhatBang(String thang) {
        model.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        // For each month, aggregate films, tickets and revenue

        for (Phim phim : quanLyPhim_Dao.getAllPhim()) {

            if (ktPhimTrongThang(phim.getMaPhim(), Integer.parseInt(thang))) {
                model.addRow(new Object[] {
                        phim.getMaPhim(),
                        phim.getTenPhim(),
                        quanLyHoaDon_DAO.tinhTongSoLuongVeTheoPhim(phim.getMaPhim()),
                        nf.format(quanLyHoaDon_DAO.tinhDoanhThuTheoPhim(phim.getMaPhim())) });
            }

        }

    }

    // return true if the film has any SuatChieu in the given month
    private boolean ktPhimTrongThang(String maPhim, int month) {

        if (quanLySuatChieu_DAO == null)
            quanLySuatChieu_DAO = new QuanLySuatChieu_DAO();
        for (SuatChieu suat : quanLySuatChieu_DAO.getAllSuatChieu()) {
            if (suat.getMaPhim().equalsIgnoreCase(maPhim) && suat.getNgayChieu().getMonthValue() == month) {
                return true;
            }
        }
        return false;
    }

    private void handleInHoaDon() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu báo cáo PDF");
            chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            String defaultName = "BaoCao_ThongKe_" + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            chooser.setSelectedFile(new File(defaultName));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
                return;
            File out = chooser.getSelectedFile();
            if (!out.getName().toLowerCase().endsWith(".pdf")) {
                out = new File(out.getParentFile(), out.getName() + ".pdf");
            }
            exportToPdf(out);
            JOptionPane.showMessageDialog(this, "Đã lưu báo cáo PDF: " + out.getAbsolutePath(), "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToPdf(File outFile) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(outFile));
        doc.open();

        // try to find a Unicode-capable TTF to embed so Vietnamese characters render
        // correctly
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
        com.itextpdf.text.Font fontHeader = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

        Paragraph title = new Paragraph("BÁO CÁO THỐNG KÊ", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph(" ", fontNormal));

        // add meta
        doc.add(new Paragraph("Ngày xuất: " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), fontNormal));
        doc.add(new Paragraph(" ", fontNormal));

        // table: month, films, tickets, revenue
        PdfPTable table = new PdfPTable(model.getColumnCount());
        table.setWidthPercentage(100);
        // headers
        for (int c = 0; c < model.getColumnCount(); c++) {
            PdfPCell h = new PdfPCell(new Paragraph(model.getColumnName(c), fontHeader));
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h);
        }
        // rows
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                Object v = model.getValueAt(r, c);
                PdfPCell cell = new PdfPCell(new Paragraph(v == null ? "" : v.toString(), fontNormal));
                table.addCell(cell);
            }
        }
        doc.add(table);

        // totals
        doc.add(new Paragraph(" ", fontNormal));
        NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("vi-VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        doc.add(new Paragraph("Tổng phim: " + totalPhim + "   Tổng vé: " + totalVe + "   Tổng doanh thu: "
                + nf.format(totalDoanhThu), fontNormal));

        doc.close();
    }

    private void close() {
        this.dispose();
        return;
    }

}
