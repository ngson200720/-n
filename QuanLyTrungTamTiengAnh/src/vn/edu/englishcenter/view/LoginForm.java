package vn.edu.englishcenter.view;

import vn.edu.englishcenter.dao.TaiKhoanDAO;
import vn.edu.englishcenter.model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private final JTextField txtUser = new JTextField(20);
    private final JPasswordField txtPass = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Đăng nhập");
    private final JButton btnExit = new JButton("Thoát");
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public LoginForm() {
        setTitle("Đăng nhập - Quản lý Trung tâm Tiếng Anh");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 270);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ TRUNG TÂM TIẾNG ANH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(27, 54, 93));
        root.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUser.setText("admin");
        formPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPass.setText("123456");
        formPanel.add(txtPass, gbc);

        root.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnLogin.setBackground(new Color(27, 54, 93));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);
        root.add(btnPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> xuLyDangNhap());
        btnExit.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnLogin);

        setContentPane(root);
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            TaiKhoan tk = taiKhoanDAO.dangNhap(user, pass);
            if (tk != null) {
                JOptionPane.showMessageDialog(this,
                        "Đăng nhập thành công! Xin chào: " + tk.getTenDangNhap() + " (" + tk.getMaVaiTro() + ")",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                new MainForm(tk).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sai tên đăng nhập, mật khẩu hoặc tài khoản đang bị khóa!",
                        "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối SQL Server:\n" + ex.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
