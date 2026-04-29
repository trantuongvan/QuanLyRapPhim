package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TrangChu extends JPanel {

    public TrangChu() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ImageIcon bgIcon = new ImageIcon("icon/backGround.jpg");
        Image bgImg = bgIcon.getImage().getScaledInstance(1100, 750, Image.SCALE_SMOOTH);
        JLabel lblBg = new JLabel(new ImageIcon(bgImg));
        lblBg.setHorizontalAlignment(JLabel.CENTER);

        Box boxInfo = Box.createVerticalBox();
        boxInfo.setBorder(new EmptyBorder(40, 30, 40, 30));
        boxInfo.setBackground(new Color(245, 245, 245));
        boxInfo.setOpaque(true);

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM 3T CINEMA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(200, 0, 0));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        boxInfo.add(lblTitle);
        boxInfo.add(Box.createVerticalStrut(40));

        JLabel lblSub = new JLabel("Trải nghiệm điện ảnh đỉnh cao cùng công nghệ 3D, IMAX");
        lblSub.setFont(new Font("Arial", Font.ITALIC, 18));
        lblSub.setForeground(Color.DARK_GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxInfo.add(lblSub);
        boxInfo.add(Box.createVerticalStrut(40));

        ImageIcon iconManager = new ImageIcon("icon/employee.png");
        Image imgManager = iconManager.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel lblManager = new JLabel(new ImageIcon(imgManager));
        lblManager.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxInfo.add(lblManager);
        boxInfo.add(Box.createVerticalStrut(15));

        JLabel lblName = new JLabel("QUẢN LÝ RẠP: NGUYỄN CHÍ TÂM");
        lblName.setFont(new Font("Arial", Font.BOLD, 18));
        lblName.setForeground(new Color(0, 0, 128));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxInfo.add(lblName);

        boxInfo.add(Box.createVerticalStrut(20));

        JLabel lblContact = new JLabel("Liên hệ: 0000 000 000 | Email: 3TCinema@gmail.com");
        lblContact.setFont(new Font("Arial", Font.PLAIN, 15));
        lblContact.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxInfo.add(lblContact);

        boxInfo.add(Box.createVerticalStrut(30));

        JLabel lblQuote = new JLabel("\"Phim hay, cảm xúc thật — chỉ có tại 3T Cinema!\"");
        lblQuote.setFont(new Font("Arial", Font.ITALIC, 18));
        lblQuote.setForeground(new Color(128, 0, 128));
        lblQuote.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxInfo.add(lblQuote);

        add(lblBg, BorderLayout.CENTER);
        add(boxInfo, BorderLayout.EAST);
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
