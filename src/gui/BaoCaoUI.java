package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import dao.QuanLyHoaDon_DAO;
import dao.QuanLyPhim_DAO;
import dao.QuanLySuatChieu_DAO;
import entity.Phim;
import entity.SuatChieu;

import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;

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

        tinhTongThongKe(thang);

        setTitle("Chi Tiết Báo Cáo");
        setSize(800, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
//        setResizable(true);

        Color darkBg = new Color(34, 34, 34);
        Color orangeColor = new Color(245, 140, 0);
        Color redColor = new Color(175, 25, 25);

        JPanel contentPane = new JPanel(new BorderLayout(0, 20));
        contentPane.setBackground(darkBg);
        contentPane.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(contentPane);

        JLabel lblTieuDe = new JLabel("BÁO CÁO THÁNG " + thang);
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnCenter = new JPanel(new BorderLayout(0, 20));
        pnCenter.setOpaque(false);
        contentPane.add(pnCenter, BorderLayout.CENTER);

        JPanel pnSummary = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        pnSummary.setOpaque(false);
        pnSummary.setBorder(new EmptyBorder(15, 20, 15, 20));

        Font fontLbl = new Font("Tahoma", Font.BOLD, 16);
        Font fontVal = new Font("Tahoma", Font.BOLD, 18);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridy = 0;

        JLabel lblNgay = new JLabel("Ngày lập báo cáo:");
        lblNgay.setFont(fontLbl);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnSummary.add(lblNgay, gbc);

        JLabel valNgay = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        valNgay.setFont(fontVal);
        valNgay.setForeground(orangeColor);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnSummary.add(valNgay, gbc);

        JLabel lblVe = new JLabel("Tổng số vé đã bán:");
        lblVe.setFont(fontLbl);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnSummary.add(lblVe, gbc);

        JLabel valVe = new JLabel(String.valueOf(totalVe));
        valVe.setFont(fontVal);
        valVe.setForeground(orangeColor);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnSummary.add(valVe, gbc);

        gbc.gridy = 1;

        JLabel lblPhim = new JLabel("Tổng số phim:");
        lblPhim.setFont(fontLbl);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        pnSummary.add(lblPhim, gbc);

        JLabel valPhim = new JLabel(String.valueOf(totalPhim));
        valPhim.setFont(fontVal);
        valPhim.setForeground(orangeColor);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnSummary.add(valPhim, gbc);

        JLabel lblDoanhThu = new JLabel("Tổng doanh thu:");
        lblDoanhThu.setFont(fontLbl);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pnSummary.add(lblDoanhThu, gbc);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        doanhThu = nf.format(totalDoanhThu);

        JLabel valDoanhThu = new JLabel(doanhThu + " đ");
        valDoanhThu.setFont(fontVal);
        valDoanhThu.setForeground(redColor);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        pnSummary.add(valDoanhThu, gbc);

        pnCenter.add(pnSummary, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[] { "Mã phim", "Tên Phim", "Số Lượng Vé", "Doanh Thu (vnđ)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 15));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(redColor);
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        capNhatBang(thang);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        pnCenter.add(scrollPane, BorderLayout.CENTER);

        JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        pnSouth.setOpaque(false);

        JButton btnDong = taoNutBoGoc("Đóng", redColor);
        JButton btnInBaoCao = taoNutBoGoc("In Báo Cáo PDF", orangeColor);

        btnDong.setPreferredSize(new Dimension(140, 45));
        btnInBaoCao.setPreferredSize(new Dimension(170, 45));

        btnDong.addActionListener(e -> close());
        btnInBaoCao.addActionListener(e -> handleInHoaDon());

        pnSouth.add(btnDong);
        pnSouth.add(btnInBaoCao);

        contentPane.add(pnSouth, BorderLayout.SOUTH);

        setVisible(true);
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

        doc.add(new Paragraph("Ngày xuất: " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), fontNormal));
        doc.add(new Paragraph(" ", fontNormal));

        PdfPTable table = new PdfPTable(model.getColumnCount());
        table.setWidthPercentage(100);
        for (int c = 0; c < model.getColumnCount(); c++) {
            PdfPCell h = new PdfPCell(new Paragraph(model.getColumnName(c), fontHeader));
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h);
        }
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                Object v = model.getValueAt(r, c);
                PdfPCell cell = new PdfPCell(new Paragraph(v == null ? "" : v.toString(), fontNormal));
                table.addCell(cell);
            }
        }
        doc.add(table);

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
    }
}