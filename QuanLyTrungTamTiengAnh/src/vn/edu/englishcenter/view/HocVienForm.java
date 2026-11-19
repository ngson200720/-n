package vn.edu.englishcenter.view;

import vn.edu.englishcenter.dao.HocVienDAO;
import vn.edu.englishcenter.model.HocVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class HocVienForm extends JFrame {
    private final MainForm parent;
    private final HocVienDAO dao = new HocVienDAO();

    private final JTextField txtMaHV = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtNgaySinh = new JTextField("2004-01-01");
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField txtSDT = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JTextField txtPhuHuynh = new JTextField();
    private final JTextField txtDiemTest = new JTextField("0.0");
    private final JComboBox<String> cboTrangThai = new JComboBox<>(
            new String[]{"Đang học", "Bảo lưu", "Đã tốt nghiệp", "Nghỉ học"});
    private final JTextField txtTimKiem = new JTextField(22);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Mã HV", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Địa chỉ", "Phụ huynh", "Điểm Test", "Trạng thái"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public HocVienForm(MainForm parent) {
        this.parent = parent;
        setTitle("Quản lý Học viên");
        setSize(1080, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadTable(null);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top: Form input
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 8));
        formPanel.setBorder(new TitledBorder("Thông tin chi tiết Học viên"));

        formPanel.add(new JLabel("Mã học viên (*):")); formPanel.add(txtMaHV);
        formPanel.add(new JLabel("Họ và tên (*):")); formPanel.add(txtHoTen);

        formPanel.add(new JLabel("Ngày sinh (yyyy-MM-dd) (*):")); formPanel.add(txtNgaySinh);
        formPanel.add(new JLabel("Giới tính:")); formPanel.add(cboGioiTinh);

        formPanel.add(new JLabel("Số điện thoại (*):")); formPanel.add(txtSDT);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);

        formPanel.add(new JLabel("Địa chỉ:")); formPanel.add(txtDiaChi);
        formPanel.add(new JLabel("Họ tên phụ huynh:")); formPanel.add(txtPhuHuynh);

        formPanel.add(new JLabel("Điểm Test đầu vào (0-10):")); formPanel.add(txtDiemTest);
        formPanel.add(new JLabel("Trạng thái:")); formPanel.add(cboTrangThai);

        // Action & Search bar
        JPanel controlPanel = new JPanel(new BorderLayout(10, 5));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnThem = new JButton("Thêm mới");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 5));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtTimKiem);
        JButton btnTim = new JButton("Tìm");
        searchPanel.add(btnTim);

        controlPanel.add(btnPanel, BorderLayout.WEST);
        controlPanel.add(searchPanel, BorderLayout.EAST);

        JPanel northContainer = new JPanel(new BorderLayout(5, 8));
        northContainer.add(formPanel, BorderLayout.CENTER);
        northContainer.add(controlPanel, BorderLayout.SOUTH);

        root.add(northContainer, BorderLayout.NORTH);

        // Center: Table
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        // Events
        btnThem.addActionListener(e -> themHocVien());
        btnSua.addActionListener(e -> suaHocVien());
        btnXoa.addActionListener(e -> xoaHocVien());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTable(null);
        });
        btnTim.addActionListener(e -> loadTable(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> loadTable(txtTimKiem.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillFormFromRow(table.getSelectedRow());
            }
        });

        setContentPane(root);
    }

    private void loadTable(String keyword) {
        try {
            List<HocVien> list = (keyword == null || keyword.trim().isEmpty())
                    ? dao.getAll()
                    : dao.search(keyword);
            tableModel.setRowCount(0);
            for (HocVien hv : list) {
                tableModel.addRow(new Object[]{
                        hv.getMaHV(), hv.getHoTen(), hv.getNgaySinh(), hv.getGioiTinh(),
                        hv.getSoDienThoai(), hv.getEmail(), hv.getDiaChi(),
                        hv.getHoTenPhuHuynh(), hv.getDiemTestDauVao(), hv.getTrangThai()
                });
            }
            if (parent != null) parent.taiThongKe();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách học viên: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private HocVien readForm() throws IllegalArgumentException {
        String ma = txtMaHV.getText().trim();
        String ten = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
            throw new IllegalArgumentException("Mã HV, Họ tên và Số điện thoại không được để trống!");
        }

        Date ngaySinh;
        try {
            ngaySinh = Date.valueOf(txtNgaySinh.getText().trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Ngày sinh không đúng định dạng yyyy-MM-dd (VD: 2004-11-02)!");
        }

        BigDecimal diem;
        try {
            diem = new BigDecimal(txtDiemTest.getText().trim());
            if (diem.compareTo(BigDecimal.ZERO) < 0 || diem.compareTo(new BigDecimal("10")) > 0) {
                throw new IllegalArgumentException("Điểm Test đầu vào phải từ 0 đến 10!");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Điểm Test đầu vào phải là số hợp lệ!");
        }

        return new HocVien(
                ma, ten, ngaySinh,
                (String) cboGioiTinh.getSelectedItem(),
                sdt,
                txtEmail.getText().trim(),
                txtDiaChi.getText().trim(),
                txtPhuHuynh.getText().trim(),
                diem,
                (String) cboTrangThai.getSelectedItem()
        );
    }

    private void themHocVien() {
        try {
            HocVien hv = readForm();
            if (dao.insert(hv)) {
                JOptionPane.showMessageDialog(this, "Thêm học viên thành công!");
                clearForm();
                loadTable(null);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm học viên (trùng mã/email hoặc lỗi CSDL):\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaHocVien() {
        try {
            HocVien hv = readForm();
            if (dao.update(hv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin học viên thành công!");
                loadTable(null);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã học viên để cập nhật!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật học viên:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaHocVien() {
        String maHV = txtMaHV.getText().trim();
        if (maHV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên cần xóa trên bảng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn chắc chắn muốn xóa học viên mã [" + maHV + "]?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.delete(maHV)) {
                    JOptionPane.showMessageDialog(this, "Xóa học viên thành công!");
                    clearForm();
                    loadTable(null);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa học viên (có thể học viên đã phát sinh dữ liệu đăng ký lớp học):\n" + ex.getMessage(),
                        "Lỗi ràng buộc SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillFormFromRow(int row) {
        txtMaHV.setText(getStr(row, 0));
        txtMaHV.setEditable(false);
        txtHoTen.setText(getStr(row, 1));
        txtNgaySinh.setText(getStr(row, 2));
        cboGioiTinh.setSelectedItem(getStr(row, 3));
        txtSDT.setText(getStr(row, 4));
        txtEmail.setText(getStr(row, 5));
        txtDiaChi.setText(getStr(row, 6));
        txtPhuHuynh.setText(getStr(row, 7));
        txtDiemTest.setText(getStr(row, 8));
        cboTrangThai.setSelectedItem(getStr(row, 9));
    }

    private String getStr(int row, int col) {
        Object v = tableModel.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private void clearForm() {
        txtMaHV.setText("");
        txtMaHV.setEditable(true);
        txtHoTen.setText("");
        txtNgaySinh.setText("2004-01-01");
        cboGioiTinh.setSelectedIndex(0);
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtPhuHuynh.setText("");
        txtDiemTest.setText("0.0");
        cboTrangThai.setSelectedIndex(0);
        txtTimKiem.setText("");
        table.clearSelection();
    }
}
