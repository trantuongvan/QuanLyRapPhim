package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ConnectDB.ConnectDB;
import dao.DangNhap_DAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class DangNhap extends JFrame implements ActionListener {
    public static NhanVien nhanVienDangNhap;
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JLabel lblTaiKhoan, lblMatKhau, lblTieuDe;
    private JButton btnDangNhap, btnThoat;
    private JCheckBox chkHienMatKhau;
    private DangNhap_DAO dangNhapDAO;

    public DangNhap() {

        dangNhapDAO = new DangNhap_DAO();

        setTitle("Đăng nhập hệ thống bán vé rạp chiếu phim");
        setSize(550, 420);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color darkBackground = new Color(34, 34, 34);
        Color orangeColor = new Color(245, 140, 0);
        Color redColor = new Color(175, 25, 25);
        Color textColor = Color.WHITE;

        Font fontTitle = new Font("Tahoma", Font.BOLD, 28);
        Font fontLabel = new Font("Tahoma", Font.PLAIN, 18);
        Font fontInput = new Font("Tahoma", Font.PLAIN, 15);

        getContentPane().setBackground(darkBackground);
        setLayout(new BorderLayout());

        JPanel pnlTieuDe = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTieuDe.setBackground(darkBackground);
        pnlTieuDe.setBorder(new EmptyBorder(35, 0, 20, 0));

        lblTieuDe = new JLabel("Đăng nhập");
        lblTieuDe.setFont(fontTitle);
        lblTieuDe.setForeground(orangeColor);
        pnlTieuDe.add(lblTieuDe);

        add(pnlTieuDe, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(darkBackground);
        pnlCenter.setBorder(new EmptyBorder(10, 60, 10, 60));

        Dimension labelSize = new Dimension(170, 32);
        Dimension inputSize = new Dimension(260, 32);
        Dimension rowSize = new Dimension(430, 32);

        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row1.setBackground(darkBackground);
        row1.setMaximumSize(rowSize);

        lblTaiKhoan = new JLabel("Tài khoản");
        lblTaiKhoan.setFont(fontLabel);
        lblTaiKhoan.setForeground(textColor);
        lblTaiKhoan.setPreferredSize(labelSize);
        lblTaiKhoan.setMaximumSize(labelSize);

        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setPreferredSize(inputSize);
        txtTaiKhoan.setMaximumSize(inputSize);
        txtTaiKhoan.setBackground(orangeColor);
        txtTaiKhoan.setForeground(Color.BLACK);
        txtTaiKhoan.setFont(fontInput);
        txtTaiKhoan.setBorder(new EmptyBorder(0, 10, 0, 10));
        txtTaiKhoan.setCaretColor(Color.BLACK);

        row1.add(lblTaiKhoan);
        row1.add(txtTaiKhoan);
        pnlCenter.add(row1);
        pnlCenter.add(Box.createVerticalStrut(18));

        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
        row2.setBackground(darkBackground);
        row2.setMaximumSize(rowSize);

        lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(fontLabel);
        lblMatKhau.setForeground(textColor);
        lblMatKhau.setPreferredSize(labelSize);
        lblMatKhau.setMaximumSize(labelSize);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setPreferredSize(inputSize);
        txtMatKhau.setMaximumSize(inputSize);
        txtMatKhau.setBackground(orangeColor);
        txtMatKhau.setForeground(Color.BLACK);
        txtMatKhau.setFont(fontInput);
        txtMatKhau.setBorder(new EmptyBorder(0, 10, 0, 10));
        txtMatKhau.setCaretColor(Color.BLACK);

        row2.add(lblMatKhau);
        row2.add(txtMatKhau);
        pnlCenter.add(row2);
        pnlCenter.add(Box.createVerticalStrut(18));

        JPanel row3 = new JPanel();
        row3.setLayout(new BoxLayout(row3, BoxLayout.X_AXIS));
        row3.setBackground(darkBackground);
        row3.setMaximumSize(rowSize);

        JLabel lblHienMatKhau = new JLabel("Hiển thị mật khẩu");
        lblHienMatKhau.setFont(fontLabel);
        lblHienMatKhau.setForeground(textColor);
        lblHienMatKhau.setPreferredSize(labelSize);
        lblHienMatKhau.setMaximumSize(labelSize);

        chkHienMatKhau = new JCheckBox();
        chkHienMatKhau.setBackground(darkBackground);
        chkHienMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));

        row3.add(lblHienMatKhau);
        row3.add(chkHienMatKhau);
        row3.add(Box.createHorizontalGlue());
        pnlCenter.add(row3);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        pnlSouth.setBackground(darkBackground);
        pnlSouth.setBorder(new EmptyBorder(10, 0, 40, 0));

        // Gọi hàm vẽ nút bo góc tùy chỉnh
        btnDangNhap = taoNutBoGoc("Đăng nhập", orangeColor);
        btnThoat = taoNutBoGoc("Thoát", redColor);

        pnlSouth.add(btnDangNhap);
        pnlSouth.add(btnThoat);

        add(pnlSouth, BorderLayout.SOUTH);

        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        chkHienMatKhau.addActionListener(this);

        txtMatKhau.addActionListener(e -> xuLyDangNhap());
        txtTaiKhoan.addActionListener(e -> txtMatKhau.requestFocus());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThoat) {
            ConnectDB.disconnect();
            System.exit(0);
        } else if (src == btnDangNhap) {
            xuLyDangNhap();
        } else if (src == chkHienMatKhau) {
            if (chkHienMatKhau.isSelected()) {
                txtMatKhau.setEchoChar((char) 0);
            } else {
                txtMatKhau.setEchoChar('•');
            }
        }
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
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(130, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void xuLyDangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhauNhap = new String(txtMatKhau.getPassword());

        TaiKhoan tk = dangNhapDAO.ktDangNhap(taiKhoan, matKhauNhap);

        if (tk != null) {
            DangNhap.nhanVienDangNhap = tk.getNhanVien();

            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

            new Start(tk).setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Tên tài khoản hoặc mật khẩu không đúng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            txtTaiKhoan.requestFocus();
        }
    }

    public DangNhap_DAO getDangNhapDAO() {
        return this.dangNhapDAO;
    }

    public static void main(String[] args) {
        new DangNhap();
    }
}