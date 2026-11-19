package vn.edu.englishcenter.view;

import vn.edu.englishcenter.dao.ThongKeDAO;
import vn.edu.englishcenter.model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class MainForm extends JFrame {
    private final TaiKhoan currentUser;
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    private final JLabel lblSoHV = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblSoKH = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblSoLop = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblDoanhThu = new JLabel("0 VNĐ", SwingConstants.CENTER);

    public MainForm(TaiKhoan user) {
        this.currentUser = user;
        setTitle("Trang chủ - Hệ thống Quản lý Trung tâm Tiếng Anh");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 540);
        setLocationRelativeTo(null);
        initMenuBar();
        initUI();
        taiThongKe();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu mHeThong = new JMenu("Hệ thống");
        JMenuItem miLamMoi = new JMenuItem("Làm mới thống kê");
        JMenuItem miDangXuat = new JMenuItem("Đăng xuất");
        JMenuItem miThoat = new JMenuItem("Thoát");
        miLamMoi.addActionListener(e -> taiThongKe());
        miDangXuat.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        miThoat.addActionListener(e -> System.exit(0));
        mHeThong.add(miLamMoi);
        mHeThong.add(miDangXuat);
        mHeThong.addSeparator();
        mHeThong.add(miThoat);

        JMenu mQuanLy = new JMenu("Quản lý nghiệp vụ");
        JMenuItem miHocVien = new JMenuItem("Quản lý Học viên");
        JMenuItem miKhoaHoc = new JMenuItem("Quản lý Khóa học");
        JMenuItem miLopHoc = new JMenuItem("Quản lý Lớp học");
        miHocVien.addActionListener(e -> moFormHocVien());
        miKhoaHoc.addActionListener(e -> moFormKhoaHoc());
        miLopHoc.addActionListener(e -> moFormLopHoc());
        mQuanLy.add(miHocVien);
        mQuanLy.add(miKhoaHoc);
        mQuanLy.add(miLopHoc);

        menuBar.add(mHeThong);
        menuBar.add(mQuanLy);
        setJMenuBar(menuBar);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(245, 247, 250));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(27, 54, 93));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("BẢNG ĐIỀU KHIỂN QUẢN LÝ TRUNG TÂM TIẾNG ANH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Người dùng: " + currentUser.getTenDangNhap() + " [" + currentUser.getMaVaiTro() + "]");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(220, 230, 245));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Center: Dashboard Cards + Navigation Buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 15, 20));
        centerPanel.setOpaque(false);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setOpaque(false);
        statsPanel.add(taoTheThongKe("TỔNG HỌC VIÊN", lblSoHV, new Color(41, 128, 185)));
        statsPanel.add(taoTheThongKe("TỔNG KHÓA HỌC", lblSoKH, new Color(39, 174, 96)));
        statsPanel.add(taoTheThongKe("TỔNG LỚP HỌC", lblSoLop, new Color(142, 68, 173)));
        statsPanel.add(taoTheThongKe("TỔNG DOANH THU", lblDoanhThu, new Color(211, 84, 0)));

        JPanel navPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        navPanel.setOpaque(false);
        JButton btnHV = taoNutChucNang("QUẢN LÝ HỌC VIÊN", "Thêm, sửa, xóa, tìm kiếm hồ sơ học viên");
        JButton btnKH = taoNutChucNang("QUẢN LÝ KHÓA HỌC", "Quản lý khóa học, cấp độ, học phí niêm yết");
        JButton btnLH = taoNutChucNang("QUẢN LÝ LỚP HỌC", "Xếp lớp, gán giảng viên, phòng học, ca học");

        btnHV.addActionListener(e -> moFormHocVien());
        btnKH.addActionListener(e -> moFormKhoaHoc());
        btnLH.addActionListener(e -> moFormLopHoc());

        navPanel.add(btnHV);
        navPanel.add(btnKH);
        navPanel.add(btnLH);

        centerPanel.add(statsPanel);
        centerPanel.add(navPanel);
        root.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton btnRefresh = new JButton("Cập nhật số liệu");
        btnRefresh.addActionListener(e -> taiThongKe());
        footer.add(btnRefresh);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel taoTheThongKe(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accent),
                new EmptyBorder(15, 10, 15, 10)
        ));

        JLabel lblT = new JLabel(title, SwingConstants.CENTER);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblT.setForeground(Color.DARK_GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accent);

        card.add(lblT, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JButton taoNutChucNang(String title, String subtitle) {
        JButton btn = new JButton("<html><center><b style='font-size:13px'>" + title
                + "</b><br><br><span style='color:#555555'>" + subtitle + "</span></center></html>");
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225), 1));
        return btn;
    }

    public void taiThongKe() {
        try {
            lblSoHV.setText(String.valueOf(thongKeDAO.demTongHocVien()));
            lblSoKH.setText(String.valueOf(thongKeDAO.demTongKhoaHoc()));
            lblSoLop.setText(String.valueOf(thongKeDAO.demTongLopHoc()));
            BigDecimal doanhThu = thongKeDAO.tinhTongDoanhThu();
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            lblDoanhThu.setText(nf.format(doanhThu) + " đ");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải thống kê: " + ex.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moFormHocVien() {
        HocVienForm f = new HocVienForm(this);
        f.setVisible(true);
    }

    private void moFormKhoaHoc() {
        KhoaHocForm f = new KhoaHocForm(this);
        f.setVisible(true);
    }

    private void moFormLopHoc() {
        LopHocForm f = new LopHocForm(this);
        f.setVisible(true);
    }
}
