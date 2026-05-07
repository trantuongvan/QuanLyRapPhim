package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ConnectDB.ConnectDB;
import dao.QuanLyCTHD_DAO;
import dao.QuanLyVe_DAO;
import entity.ChiTietHoaDon;
import entity.LoadData;
import entity.Ve;
import entity.ResetForm;

public class QuanLyVe extends JPanel implements LoadData, ResetForm {
    private QuanLyVe_DAO ticketManager;
    private QuanLyCTHD_DAO cthdManager;

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaVe, txtMaGhe, txtNgayBan, txtMaSuatChieu, txtDaThanhToan, txtTimVe;
    private JButton btnXoa, btnXoaRong, btnLuu, btnTim;

    private final Font FONT_LBL = new Font("Tahoma", Font.BOLD, 18);
    private final Font FONT_TXT = new Font("Tahoma", Font.PLAIN, 18);
    private final Color orangeColor = new Color(245, 140, 0);
    private final Color panelDarkTone = new Color(50, 50, 50);
    private final Color tableBackground = new Color(40, 40, 40);

    private ArrayList<Ve> danhSach = new ArrayList<>();

    public QuanLyVe() {
        this.ticketManager = new QuanLyVe_DAO();
        this.cthdManager = new QuanLyCTHD_DAO();

        setLayout(new GridBagLayout());
        setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        JLabel lblTieuDe = new JLabel("QUẢN LÝ VÉ PHIM");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTieuDe.setForeground(orangeColor);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTieuDe, gbc);

        gbc.gridy = 1;
        JPanel pnTop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnTop.setOpaque(false);
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnTop.setPreferredSize(new Dimension(0, 70));

        txtTimVe = createStyledTextField(300);
        btnTim = taoNutBoGoc("Xem", new Color(160, 82, 45));
        btnXoa = taoNutBoGoc("Xóa", orangeColor);
        btnXoaRong = taoNutBoGoc("Xóa rỗng", orangeColor);
        btnLuu = taoNutBoGoc("Lưu", orangeColor);

        JLabel lblTim = new JLabel("Tìm vé: ");
        lblTim.setForeground(Color.WHITE);
        lblTim.setFont(new Font("Tahoma", Font.BOLD, 16));

        pnTop.add(lblTim);
        pnTop.add(txtTimVe);
        pnTop.add(btnTim);
        pnTop.add(btnXoa);
        pnTop.add(btnXoaRong);
        pnTop.add(btnLuu);
        add(pnTop, gbc);

        gbc.gridy = 2;
        JPanel pnInput = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelDarkTone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        pnInput.setOpaque(false);
        pnInput.setLayout(new GridBagLayout());

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 20, 10, 20);
        innerGbc.anchor = GridBagConstraints.WEST;

        txtMaVe = createStyledTextField(200);
        txtMaGhe = createStyledTextField(200);
        txtNgayBan = createStyledTextField(200);
        txtMaSuatChieu = createStyledTextField(200);
        txtDaThanhToan = createStyledTextField(200);

        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.gridwidth = 4;
        innerGbc.anchor = GridBagConstraints.CENTER;
        JLabel lblHeader = new JLabel("THÔNG TIN VÉ PHIM");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeader.setForeground(Color.WHITE);
        pnInput.add(lblHeader, innerGbc);

        innerGbc.gridwidth = 1;
        innerGbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Mã vé:", "Mã suất chiếu:", "Mã ghế:", "Trạng thái:", "Ngày bán:"};
        JLabel[] jLabels = new JLabel[labels.length];
        for (int i = 0; i < labels.length; i++) {
            jLabels[i] = new JLabel(labels[i]);
            jLabels[i].setForeground(Color.WHITE);
            jLabels[i].setFont(new Font("Tahoma", Font.BOLD, 14));
        }

        innerGbc.gridy = 1;
        innerGbc.gridx = 0;
        pnInput.add(jLabels[0], innerGbc);
        innerGbc.gridx = 1;
        innerGbc.weightx = 0.5;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        pnInput.add(txtMaVe, innerGbc);

        innerGbc.gridx = 2;
        innerGbc.weightx = 0;
        innerGbc.fill = GridBagConstraints.NONE;
        pnInput.add(jLabels[1], innerGbc);
        innerGbc.gridx = 3;
        innerGbc.weightx = 0.5;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        pnInput.add(txtMaSuatChieu, innerGbc);

        innerGbc.gridy = 2;
        innerGbc.gridx = 0;
        innerGbc.weightx = 0;
        innerGbc.fill = GridBagConstraints.NONE;
        pnInput.add(jLabels[2], innerGbc);
        innerGbc.gridx = 1;
        innerGbc.weightx = 0.5;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        pnInput.add(txtMaGhe, innerGbc);

        innerGbc.gridx = 2;
        innerGbc.weightx = 0;
        innerGbc.fill = GridBagConstraints.NONE;
        pnInput.add(jLabels[3], innerGbc);
        innerGbc.gridx = 3;
        innerGbc.weightx = 0.5;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        pnInput.add(txtDaThanhToan, innerGbc);

        innerGbc.gridy = 3;
        innerGbc.gridx = 0;
        innerGbc.weightx = 0;
        innerGbc.fill = GridBagConstraints.NONE;
        pnInput.add(jLabels[4], innerGbc);
        innerGbc.gridx = 1;
        innerGbc.weightx = 0.5;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        pnInput.add(txtNgayBan, innerGbc);

        add(pnInput, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        model = new DefaultTableModel(new String[]{"STT", "Mã vé", "Mã ghế", "Ngày bán", "Mã suất chiếu", "Trạng thái"}, 0);
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

        btnXoa.addActionListener(e -> xoaHoaDon());
        btnXoaRong.addActionListener(e -> resetForm());
        btnLuu.addActionListener(e -> luu());
        btnTim.addActionListener(e -> timHoaDon());
        table.getSelectionModel().addListSelectionListener(e -> hienThiLenForm());

        loadData();
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
        tf.setCaretColor(Color.BLACK);
        tf.setPreferredSize(new Dimension(width, 35));
        return tf;
    }

    private JButton taoNutBoGoc(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHovered) {
                    g2.setColor(bgColor.darker());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    @Override
    public void loadData() {
        if (ticketManager != null) {
            danhSach = ticketManager.getDanhSachVe();
            refreshTable();
        }
    }

    private void refreshTable() {
        if (model == null) return;
        model.setRowCount(0);
        int count = 1;
        for (Ve ve : danhSach) {
            model.addRow(new Object[]{count++, ve.getMaVe(), ve.getGhe().getMaGhe(), ve.getNgayBan(), ve.getMaSuatChieu(), ve.getTrangThai()});
        }
    }

    private void xoaHoaDon() {
        String ma = this.txtMaVe.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã vé cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa vé phim này " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (this.ticketManager.XoaVeTheoMa(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy vé cần xóa!");
            }
        }
    }

    private void timHoaDon() {
        String ma = this.txtTimVe.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã vé cần tìm!");
            return;
        }
        ChiTietHoaDon cthd = this.cthdManager.TimCTHDTheoMaVe(ma);
        if (cthd != null) {
            ArrayList<String> danhSachGhe = new ArrayList<>();
            danhSachGhe.add(cthd.getVe().getGhe().getTenGhe());
            new ThongTinVeModal(cthd.getHoaDon(), cthd.getVe(), this, danhSachGhe);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy vé có mã " + ma);
        }
    }

    private void hienThiLenForm() {
        int i = table.getSelectedRow();
        if (i >= 0 && i < this.danhSach.size()) {
            Ve p = this.danhSach.get(i);
            this.txtMaVe.setText(p.getMaVe());
            this.txtTimVe.setText(p.getMaVe());
            this.txtMaGhe.setText(p.getGhe().getMaGhe());
            this.txtNgayBan.setText(p.getNgayBan().toString());
            this.txtMaSuatChieu.setText(p.getMaSuatChieu());
            this.txtDaThanhToan.setText(String.valueOf(p.getTrangThai()));
        }
    }

    @Override
    public void resetForm() {
        txtMaVe.setText("");
        txtMaGhe.setText("");
        txtNgayBan.setText("");
        txtMaSuatChieu.setText("");
        txtDaThanhToan.setText("");
        txtTimVe.setText("");
    }

    private void luu() {
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu vào CSDL!");
    }
}