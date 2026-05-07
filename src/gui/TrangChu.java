package gui;

import entity.NhanVien;
import entity.TaiKhoan;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String path) {
        backgroundImage = new ImageIcon(path).getImage();
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

public class TrangChu extends JPanel {

    public TrangChu() {
        setLayout(new BorderLayout());

        // Panel nền
        BackgroundPanel bgPanel = new BackgroundPanel("icon/bg.jpg");
        bgPanel.setLayout(new GridBagLayout()); // để căn giữa content

        // ===== CONTENT (đè lên) =====
        JPanel content = new JPanel();
        content.setOpaque(false); // QUAN TRỌNG -> trong suốt
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel titleTop = new JPanel();

        JLabel title1 = new JLabel("Hệ thống quản lý rạp chiếu phim ");
        title1.setForeground(Color.WHITE);
        title1.setFont(new Font("Tahoma", Font.BOLD, 36));
        JLabel title2 = new JLabel("AbsoluteCinema");
        title2.setForeground(new Color(255, 144, 0));
        title2.setFont(new Font("Tahoma", Font.BOLD, 36));

        titleTop.add(title1);
        titleTop.add(title2);
        titleTop.setOpaque(false);
        titleTop.setAlignmentX(Component.CENTER_ALIGNMENT);


        ImageIcon icon = new ImageIcon("icon/logo.png");
        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel avatar = new JLabel(new ImageIcon(img));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel("Xin kính chào");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Tahoma", Font.BOLD, 36));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);


        TaiKhoan taiKhoan = Start.taiKhoanHienTai;
        NhanVien nhanVien = DangNhap.nhanVienDangNhap;
        String vaiTro = taiKhoan.getVaiTro().toString();
        JPanel panelNguoiDangNhap = new JPanel();
        JLabel labelVaiTro;
        String tenDangNhap = nhanVien.getTenNV();
        JLabel labelName = new JLabel(tenDangNhap.toUpperCase());
        if (vaiTro.equalsIgnoreCase("QUAN_LY")) {
            labelVaiTro = new JLabel("Quản lý rạp: ");
        } else {
            labelVaiTro = new JLabel("Nhân viên rạp: ");
        }
        labelVaiTro.setForeground(Color.WHITE);
        labelVaiTro.setFont(new Font("Segoe UI", Font.BOLD, 36));
        labelName.setForeground(new Color(255, 144, 0));
        labelName.setFont(new Font("Segoe UI", Font.BOLD, 36));

        panelNguoiDangNhap.add(labelVaiTro);
        panelNguoiDangNhap.add(labelName);
        panelNguoiDangNhap.setOpaque(false);
        panelNguoiDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contact = new JLabel("Liên hệ: 0912 345 678 | Email: abc@gmail.com");
        contact.setForeground(Color.WHITE);
        contact.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        contact.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add vào content
        content.add(titleTop);
        content.add(Box.createVerticalStrut(20));
        content.add(avatar);
        content.add(Box.createVerticalStrut(20));
        content.add(name);
        content.add(panelNguoiDangNhap);
        content.add(Box.createVerticalStrut(10));
        content.add(contact);

        // add content lên background
        bgPanel.add(content);

        add(bgPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Trang chủ - 3T Cinema");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1250, 750);
        f.add(new TrangChu());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
