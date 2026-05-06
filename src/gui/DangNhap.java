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
        setTitle("Đăng nhập hệ thống bán vé rạp chiếu phim");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dangNhapDAO = new DangNhap_DAO();

        JPanel pnlTieuDe = new JPanel();
        lblTieuDe = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 32));
        lblTieuDe.setForeground(Color.WHITE);
        pnlTieuDe.setBackground(Color.RED);
        pnlTieuDe.add(lblTieuDe);

        Box boxTong = Box.createVerticalBox();
        Box bTaiKhoan, bMatKhau, bCheck, bNut;

        boxTong.add(Box.createVerticalStrut(40));
        boxTong.add(bTaiKhoan = Box.createHorizontalBox());
        bTaiKhoan.add(lblTaiKhoan = new JLabel("Tài khoản:"));
        lblTaiKhoan.setFont(new Font("Tahoma", Font.BOLD, 15));
        bTaiKhoan.add(Box.createHorizontalStrut(10));
        bTaiKhoan.add(txtTaiKhoan = new JTextField(20));
        txtTaiKhoan.setMaximumSize(new Dimension(300, 30));

        boxTong.add(Box.createVerticalStrut(15));
        boxTong.add(bMatKhau = Box.createHorizontalBox());
        bMatKhau.add(lblMatKhau = new JLabel("Mật khẩu:"));
        lblMatKhau.setFont(new Font("Tahoma", Font.BOLD, 15));
        bMatKhau.add(Box.createHorizontalStrut(10));
        bMatKhau.add(txtMatKhau = new JPasswordField(20));
        txtMatKhau.setMaximumSize(new Dimension(300, 30));

        boxTong.add(Box.createVerticalStrut(15));
        boxTong.add(bCheck = Box.createHorizontalBox());
        chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");
        bCheck.add(chkHienMatKhau);

        boxTong.add(Box.createVerticalStrut(20));
        boxTong.add(bNut = Box.createHorizontalBox());
        btnDangNhap = new JButton("Đăng nhập");
        btnThoat = new JButton("Thoát");
        bNut.add(btnDangNhap);
        bNut.add(Box.createHorizontalStrut(20));
        bNut.add(btnThoat);

        JLabel lblHinh = new JLabel();
        lblHinh.setIcon(new ImageIcon("icon/icon_pass.png"));
        lblHinh.setBorder(new EmptyBorder(0, 10, 0, 0));

        add(pnlTieuDe, BorderLayout.NORTH);
        add(lblHinh, BorderLayout.WEST);
        add(boxTong, BorderLayout.CENTER);

        lblMatKhau.setPreferredSize(lblTaiKhoan.getPreferredSize());

        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        chkHienMatKhau.addActionListener(this);

        // Xu li dang nhap khi user nhan enter tren password field
        this.txtMatKhau.addActionListener(e -> xuLyDangNhap());

        setSize(600, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThoat) {
            //ngắt kết nối database trước khi thoát
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
