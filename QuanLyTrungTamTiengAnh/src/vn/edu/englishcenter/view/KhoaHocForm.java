package vn.edu.englishcenter.view;

import vn.edu.englishcenter.dao.KhoaHocDAO;
import vn.edu.englishcenter.model.ComboItem;
import vn.edu.englishcenter.model.KhoaHoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class KhoaHocForm extends JFrame {
    private final MainForm parent;
    private final KhoaHocDAO dao = new KhoaHocDAO();

    private final JTextField txtMaKH = new JTextField();
    private final JTextField txtTenKH = new JTextField();
    private final JComboBox<ComboItem> cboCapDo = new JComboBox<>();
    private final JTextField txtSoBuoi = new JTextField("24");
    private final JTextField txtThoiLuong = new JTextField("90");
    private final JTextField txtHocPhi = new JTextField("5000000");
    private final JTextField txtGiaoTrinh = new JTextField();
    private final JCheckBox chkDangMo = new JCheckBox("Đang mở tuyển sinh", true);
    private final JTextField txtTimKiem = new JTextField(22);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Mã khóa học", "Tên khóa học", "Mã cấp độ", "Số buổi", "Thời lượng (phút)", "Học phí (VNĐ)", "Giáo trình", "Trạng thái"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public KhoaHocForm(MainForm parent) {
        this.parent = parent;
        setTitle("Quản lý Khóa học");
        setSize(1020, 620);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadCapDo();
        loadTable(null);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 8));
        formPanel.setBorder(new TitledBorder("Thông tin Khóa học"));

        formPanel.add(new JLabel("Mã khóa học (*):")); formPanel.add(txtMaKH);
        formPanel.add(new JLabel("Tên khóa học (*):")); formPanel.add(txtTenKH);

        formPanel.add(new JLabel("Cấp độ đào tạo (*):")); formPanel.add(cboCapDo);
        formPanel.add(new JLabel("Tổng số buổi (>0):")); formPanel.add(txtSoBuoi);

        formPanel.add(new JLabel("Thời lượng/buổi (>=45 phút):")); formPanel.add(txtThoiLuong);
        formPanel.add(new JLabel("Học phí niêm yết (VNĐ):")); formPanel.add(txtHocPhi);

        formPanel.add(new JLabel("Giáo trình:")); formPanel.add(txtGiaoTrinh);
        formPanel.add(new JLabel("Trạng thái:")); formPanel.add(chkDangMo);

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

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        btnThem.addActionListener(e -> themKhoaHoc());
        btnSua.addActionListener(e -> suaKhoaHoc());
        btnXoa.addActionListener(e -> xoaKhoaHoc());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadTable(null);
        });
        btnTim.addActionListener(e -> loadTable(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> loadTable(txtTimKiem.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillForm(table.getSelectedRow());
            }
        });

        setContentPane(root);
    }

    private void loadCapDo() {
        try {
            cboCapDo.removeAllItems();
            for (ComboItem item : dao.getAllCapDo()) {
                cboCapDo.addItem(item);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục Cấp độ: " + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable(String keyword) {
        try {
            List<KhoaHoc> list = (keyword == null || keyword.trim().isEmpty())
                    ? dao.getAll()
                    : dao.search(keyword);
            tableModel.setRowCount(0);
            for (KhoaHoc kh : list) {
                tableModel.addRow(new Object[]{
                        kh.getMaKhoaHoc(), kh.getTenKhoaHoc(), kh.getMaCapDo(),
                        kh.getTongSoBuoi(), kh.getThoiLuongMoiBuoi(),
                        kh.getHocPhiNiemYet(), kh.getGiaoTrinh(),
                        kh.isTrangThaiMo() ? "Đang mở" : "Ngừng mở"
                });
            }
            if (parent != null) parent.taiThongKe();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khóa học: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private KhoaHoc readForm() throws IllegalArgumentException {
        String ma = txtMaKH.getText().trim();
        String ten = txtTenKH.getText().trim();
        ComboItem capDo = (ComboItem) cboCapDo.getSelectedItem();
        if (ma.isEmpty() || ten.isEmpty() || capDo == null) {
            throw new IllegalArgumentException("Mã khóa học, Tên khóa học và Cấp độ không được để trống!");
        }
        int soBuoi, thoiLuong;
        BigDecimal hocPhi;
        try {
            soBuoi = Integer.parseInt(txtSoBuoi.getText().trim());
            thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
            hocPhi = new BigDecimal(txtHocPhi.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Số buổi, thời lượng và học phí phải là giá trị số hợp lệ!");
        }
        if (soBuoi <= 0 || thoiLuong < 45 || hocPhi.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Số buổi > 0, Thời lượng >= 45 phút và Học phí >= 0!");
        }

        return new KhoaHoc(ma, ten, capDo.getValue(), soBuoi, thoiLuong, hocPhi,
                txtGiaoTrinh.getText().trim(), chkDangMo.isSelected());
    }

    private void themKhoaHoc() {
        try {
            if (dao.insert(readForm())) {
                JOptionPane.showMessageDialog(this, "Thêm khóa học thành công!");
                clearForm();
                loadTable(null);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khóa học:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhoaHoc() {
        try {
            if (dao.update(readForm())) {
                JOptionPane.showMessageDialog(this, "Cập nhật khóa học thành công!");
                loadTable(null);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã khóa học để cập nhật!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật khóa học:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKhoaHoc() {
        String ma = txtMaKH.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học cần xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa khóa học [" + ma + "]?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa khóa học thành công!");
                    clearForm();
                    loadTable(null);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa khóa học (đang có Lớp học thuộc khóa học này):\n" + ex.getMessage(),
                        "Lỗi ràng buộc SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm(int row) {
        txtMaKH.setText(getStr(row, 0));
        txtMaKH.setEditable(false);
        txtTenKH.setText(getStr(row, 1));
        selectComboByValue(cboCapDo, getStr(row, 2));
        txtSoBuoi.setText(getStr(row, 3));
        txtThoiLuong.setText(getStr(row, 4));
        txtHocPhi.setText(getStr(row, 5));
        txtGiaoTrinh.setText(getStr(row, 6));
        chkDangMo.setSelected("Đang mở".equals(getStr(row, 7)));
    }

    private void selectComboByValue(JComboBox<ComboItem> combo, String value) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getValue().equals(value)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private String getStr(int row, int col) {
        Object v = tableModel.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtMaKH.setEditable(true);
        txtTenKH.setText("");
        if (cboCapDo.getItemCount() > 0) cboCapDo.setSelectedIndex(0);
        txtSoBuoi.setText("24");
        txtThoiLuong.setText("90");
        txtHocPhi.setText("5000000");
        txtGiaoTrinh.setText("");
        chkDangMo.setSelected(true);
        txtTimKiem.setText("");
        table.clearSelection();
    }
}
