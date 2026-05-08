package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import dao.DangNhap_DAO;
import entity.LoadData;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;

public class Start extends JFrame implements ActionListener {
    private JLabel lblNhanVien;
    private JButton btnTrangChu, btnBanVe, btnPhim, btnSuatChieu,
            btnKhachHang, btnNhanVien, btnThongKe, btnDangXuat;
    private CardLayout cardLayout;
    private JButton btnHoaDon, btnVe;
    private JButton selectedButton;
    private DangNhap_DAO dangNhap_dao;
    public static TaiKhoan taiKhoanHienTai;

    public Start(TaiKhoan taiKhoan) {
        this.taiKhoanHienTai = taiKhoan;

        setTitle("Hệ thống quản lý bán vé rạp chiếu phim");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel pnWest = new JPanel(new BorderLayout());
        pnWest.setPreferredSize(new Dimension(220, 1000));
        pnWest.setBackground(new Color(17, 17, 17));

        Box boxMenu = Box.createVerticalBox();
        pnWest.add(boxMenu);
        boxMenu.add(Box.createVerticalStrut(25));

        Font fontButton = new Font("Tahoma", Font.BOLD, 20);
        Color bgMenu = pnWest.getBackground();
        Color hoverBgColor = new Color(234, 234, 234);
        Color hoverTextColor = new Color(255, 144, 0);
        Color selectedBgColor = new Color(234, 234, 234);
        Color selectedTextColor = new Color(255, 144, 0);

        btnTrangChu = new JButton("Trang chủ");
        btnBanVe = new JButton("Bán vé");
        btnPhim = new JButton("Phim");
        btnSuatChieu = new JButton("Suất chiếu");
        btnKhachHang = new JButton("Khách hàng");
        btnNhanVien = new JButton("Nhân viên");
        btnThongKe = new JButton("Thống kê");
        btnHoaDon = new JButton("Hóa đơn");
        btnVe = new JButton("Vé phim");
        btnDangXuat = new JButton("Đăng xuất");

        JButton[] menuButtons = {
                btnTrangChu, btnBanVe, btnPhim, btnSuatChieu, btnKhachHang, btnNhanVien, btnThongKe, btnHoaDon, btnVe
        };

        for (JButton btn : menuButtons) {
            btn.setBackground(bgMenu);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontButton);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setIconTextGap(5);

            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(hoverBgColor);
                    btn.setForeground(hoverTextColor);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (btn != selectedButton) {
                        btn.setBackground(bgMenu);
                        btn.setForeground(Color.WHITE);
                    } else {
                        btn.setBackground(selectedBgColor);
                        btn.setForeground(selectedTextColor);
                    }
                }
            });
        }

        btnDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDangXuat.setBackground(hoverBgColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btnDangXuat != selectedButton) {
                    btnDangXuat.setBackground(bgMenu);
                } else {
                    btnDangXuat.setBackground(selectedBgColor);
                }
            }
        });

        btnDangXuat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnDangXuat.setIconTextGap(5);
        btnDangXuat.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        btnDangXuat.setBackground(bgMenu);
        btnDangXuat.setForeground(new Color(175, 25, 25));
        btnDangXuat.setFont(fontButton);
        btnDangXuat.setHorizontalAlignment(SwingConstants.LEFT);
        btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.setFocusPainted(false);

        boxMenu.add(Box.createVerticalStrut(10));
        for (JButton btn : menuButtons) {
            boxMenu.add(btn);
        }
        boxMenu.add(btnDangXuat);

        add(pnWest, BorderLayout.WEST);

        JPanel pnCenter = new JPanel(new CardLayout());
        add(pnCenter, BorderLayout.CENTER);

        JPanel trangChuPanel = new TrangChu();
        JPanel banVePanel = new QuanLyBanVe();
        JPanel phimPanel = new QuanLyPhim();
        JPanel suatChieuPanel = new QuanLySuatChieu();
        JPanel khachHangPanel = new QuanLyKhachHang();
        JPanel nhanVienPanel = new QuanLyNhanVien();
        JPanel thongKePanel = new QuanLyThongKe();
        JPanel hoaDonPanel = new QuanLyHoaDon();
        JPanel vePanel = new QuanLyVe();

        pnCenter.add(trangChuPanel, "TrangChu");
        pnCenter.add(banVePanel, "BanVe");
        pnCenter.add(phimPanel, "Phim");
        pnCenter.add(suatChieuPanel, "SuatChieu");
        pnCenter.add(khachHangPanel, "KhachHang");
        pnCenter.add(nhanVienPanel, "NhanVien");
        pnCenter.add(thongKePanel, "ThongKe");
        pnCenter.add(hoaDonPanel, "HoaDon");
        pnCenter.add(vePanel, "Ve");

        this.cardLayout = (CardLayout) pnCenter.getLayout();
        
        btnTrangChu.addActionListener(e -> {
            showPanel(pnCenter, "TrangChu", null);
            selectButton(btnTrangChu, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnBanVe.addActionListener(e -> {
            showPanel(pnCenter, "BanVe", (LoadData) banVePanel);
            selectButton(btnBanVe, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnPhim.addActionListener(e -> {
            showPanel(pnCenter, "Phim", (LoadData) phimPanel);
            selectButton(btnPhim, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnSuatChieu.addActionListener(e -> {
            showPanel(pnCenter, "SuatChieu", (LoadData) suatChieuPanel);
            selectButton(btnSuatChieu, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnKhachHang.addActionListener(e -> {
            showPanel(pnCenter, "KhachHang", (LoadData) khachHangPanel);
            selectButton(btnKhachHang, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnNhanVien.addActionListener(e -> {
            showPanel(pnCenter, "NhanVien", (LoadData) nhanVienPanel);
            selectButton(btnNhanVien, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnThongKe.addActionListener(e -> {
            showPanel(pnCenter, "ThongKe", (LoadData) thongKePanel);
            selectButton(btnThongKe, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnHoaDon.addActionListener(e -> {
            showPanel(pnCenter, "HoaDon", (LoadData) hoaDonPanel);
            selectButton(btnHoaDon, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnVe.addActionListener(e -> {
            showPanel(pnCenter, "Ve", (LoadData) vePanel);
            selectButton(btnVe, menuButtons, bgMenu, selectedBgColor, selectedTextColor);
        });
        btnDangXuat.addActionListener(e -> {
            if (btnDangXuat == selectedButton) {
                btnDangXuat.setBackground(selectedBgColor);
            }
            else {
                btnDangXuat.setBackground(bgMenu);
            }
        });

        btnDangXuat.addActionListener(this);

        selectButton(btnTrangChu, menuButtons, bgMenu, selectedBgColor, selectedTextColor);

        phanQuyen();
    }

    private void phanQuyen() {
        if (taiKhoanHienTai != null) {
            String vaiTro = taiKhoanHienTai.getVaiTro().toString();

            if (vaiTro.equalsIgnoreCase("NHAN_VIEN")) {
                btnPhim.setVisible(false);
                btnSuatChieu.setVisible(false);
                btnNhanVien.setVisible(false);
                btnThongKe.setVisible(false);
                setTitle("Hệ thống bán vé rạp chiếu phim - Xin chào Nhân Viên");
            } else {
                setTitle("Hệ thống quản lý rạp chiếu phim - Xin chào Quản Lý");
            }
        }
    }

    private void showPanel(JPanel pnCenter, String name, LoadData loadData) {
        this.cardLayout.show(pnCenter, name);
        if (loadData != null)
            loadData.loadData();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDangXuat) {
            new DangNhap().setVisible(true);
            dispose();
        }
    }

    private void selectButton(JButton selected, JButton[] navButtons, Color bgMenu, Color selectedBgColor, Color selectedTextColor) {
        this.selectedButton = selected;

        for (JButton b : navButtons) {
            if (b == selected) {
                b.setBackground(selectedBgColor);
                b.setForeground(selectedTextColor);
            } else {
                b.setBackground(bgMenu);
                b.setForeground(Color.WHITE);
            }
        }
    }

    public static ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public JPanel createSeparatorLine(int thickness) {
        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, thickness));
        line.setPreferredSize(new Dimension(Integer.MAX_VALUE, thickness));
        line.setBackground(Color.WHITE);
        return line;
    }

    public static void main(String[] args) {
        new DangNhap().setVisible(true);
    }
}