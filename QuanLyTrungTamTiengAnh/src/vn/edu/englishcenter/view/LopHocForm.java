package vn.edu.englishcenter.view;

import vn.edu.englishcenter.dao.LopHocDAO;
import vn.edu.englishcenter.model.ComboItem;
import vn.edu.englishcenter.model.LopHoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class LopHocForm extends JFrame {
    private final MainForm parent;
    private final LopHocDAO dao = new LopHocDAO();

    private final JTextField txtMaLop = new JTextField();
    private final JTextField txtTenLop = new JTextField();
    private final JComboBox<ComboItem> cboKhoaHoc = new JComboBox<>();
    private final JComboBox<ComboItem> cboGiangVien = new JComboBox<>();
    private final JComboBox<ComboItem> cboPhongHoc = new JComboBox<>();
    private final JComboBox<ComboItem> cboCaHoc = new JComboBox<>();
    private final JTextField txtNgayKG = new JTextField("2026-12-01");
    private final JTextField txtNgayKT = new JTextField("2027-02-28");
    private final JTextField txtSiSoMax = new JTextField("15");
    private final JComboBox<String> cboTrangThai = new JComboBox<>(
            new String[]{"Đang tuyển sinh", "Đang học", "Đã kết thúc", "Đã hủy"});
    private final JTextField txtTimKiem = new JTextField(22);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Mã lớp", "Tên lớp", "Mã khóa học", "Mã GV", "Mã phòng", "Mã ca", "Khai giảng", "Kết thúc", "Sĩ số", "Trạng thái"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public LopHocForm(MainForm parent) {
        this.parent = parent;
        setTitle("Quản lý Lớp học");
        setSize(1120, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadCombos();
        loadTable(null);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 8));
        formPanel.setBorder(new TitledBorder("Thông tin Lớp học"));

        formPanel.add(new JLabel("Mã lớp (*):")); formPanel.add(txtMaLop);
        formPanel.add(new JLabel("Tên lớp (*):")); formPanel.add(txtTenLop);

        formPanel.add(new JLabel("Khóa học (*):")); formPanel.add(cboKhoaHoc);
        formPanel.add(new JLabel("Giảng viên (*):")); formPanel.add(cboGiangVien);

        formPanel.add(new JLabel("Phòng học (*):")); formPanel.add(cboPhongHoc);
        formPanel.add(new JLabel("Ca học (*):")); formPanel.add(cboCaHoc);

        formPanel.add(new JLabel("Ngày khai giảng (yyyy-MM-dd):")); formPanel.add(txtNgayKG);
        formPanel.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):")); formPanel.add(txtNgayKT);

        formPanel.add(new JLabel("Sĩ số tối đa (>0):")); formPanel.add(txtSiSoMax);
        formPanel.add(new JLabel("Trạng thái lớp:")); formPanel.add(cboTrangThai);

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

        btnThem.addActionListener(e -> themLopHoc());
        btnSua.addActionListener(e -> suaLopHoc());
        btnXoa.addActionListener(e -> xoaLopHoc());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadCombos();
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

    private void loadCombos() {
        try {
            fillCombo(cboKhoaHoc, dao.getKhoaHocCombo());
            fillCombo(cboGiangVien, dao.getGiangVienCombo());
            fillCombo(cboPhongHoc, dao.getPhongHocCombo());
            fillCombo(cboCaHoc, dao.getCaHocCombo());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu danh mục: " + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillCombo(JComboBox<ComboItem> combo, List<ComboItem> items) {
        combo.removeAllItems();
        for (ComboItem it : items) {
            combo.addItem(it);
        }
    }

    private void loadTable(String keyword) {
        try {
            List<LopHoc> list = (keyword == null || keyword.trim().isEmpty())
                    ? dao.getAll()
                    : dao.search(keyword);
            tableModel.setRowCount(0);
            for (LopHoc lh : list) {
                tableModel.addRow(new Object[]{
                        lh.getMaLop(), lh.getTenLop(), lh.getMaKhoaHoc(), lh.getMaGV(),
                        lh.getMaPhong(), lh.getMaCa(), lh.getNgayKhaiGiang(),
                        lh.getNgayKetThucDuKien(),
                        lh.getSiSoHienTai() + "/" + lh.getSiSoToiDa(),
                        lh.getTrangThaiLop()
                });
            }
            if (parent != null) parent.taiThongKe();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách lớp học: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LopHoc readForm() throws IllegalArgumentException {
        String ma = txtMaLop.getText().trim();
        String ten = txtTenLop.getText().trim();
        ComboItem kh = (ComboItem) cboKhoaHoc.getSelectedItem();
        ComboItem gv = (ComboItem) cboGiangVien.getSelectedItem();
        ComboItem ph = (ComboItem) cboPhongHoc.getSelectedItem();
        ComboItem ca = (ComboItem) cboCaHoc.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty() || kh == null || gv == null || ph == null || ca == null) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin lớp học và chọn các danh mục liên quan!");
        }

        Date ngayKG, ngayKT;
        try {
            ngayKG = Date.valueOf(txtNgayKG.getText().trim());
            ngayKT = Date.valueOf(txtNgayKT.getText().trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Ngày khai giảng / kết thúc phải đúng định dạng yyyy-MM-dd!");
        }
        if (!ngayKT.after(ngayKG)) {
            throw new IllegalArgumentException("Ngày kết thúc dự kiến phải lớn hơn ngày khai giảng!");
        }

        int siSoMax;
        try {
            siSoMax = Integer.parseInt(txtSiSoMax.getText().trim());
            if (siSoMax <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Sĩ số tối đa phải là số nguyên dương!");
        }

        return new LopHoc(ma, ten, kh.getValue(), gv.getValue(), ph.getValue(), ca.getValue(),
                ngayKG, ngayKT, siSoMax, 0, (String) cboTrangThai.getSelectedItem());
    }

    private void themLopHoc() {
        try {
            if (dao.insert(readForm())) {
                JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
                clearForm();
                loadTable(null);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm lớp học:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaLopHoc() {
        try {
            if (dao.update(readForm())) {
                JOptionPane.showMessageDialog(this, "Cập nhật lớp học thành công!");
                loadTable(null);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã lớp để cập nhật!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật lớp học:\n" + ex.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaLopHoc() {
        String ma = txtMaLop.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học cần xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa lớp học [" + ma + "]?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa lớp học thành công!");
                    clearForm();
                    loadTable(null);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa lớp học (đã có học viên đăng ký lớp này):\n" + ex.getMessage(),
                        "Lỗi ràng buộc SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm(int row) {
        txtMaLop.setText(getStr(row, 0));
        txtMaLop.setEditable(false);
        txtTenLop.setText(getStr(row, 1));
        selectComboByValue(cboKhoaHoc, getStr(row, 2));
        selectComboByValue(cboGiangVien, getStr(row, 3));
        selectComboByValue(cboPhongHoc, getStr(row, 4));
        selectComboByValue(cboCaHoc, getStr(row, 5));
        txtNgayKG.setText(getStr(row, 6));
        txtNgayKT.setText(getStr(row, 7));
        String siSoStr = getStr(row, 8);
        if (siSoStr.contains("/")) {
            txtSiSoMax.setText(siSoStr.split("/")[1]);
        }
        cboTrangThai.setSelectedItem(getStr(row, 9));
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
        txtMaLop.setText("");
        txtMaLop.setEditable(true);
        txtTenLop.setText("");
        if (cboKhoaHoc.getItemCount() > 0) cboKhoaHoc.setSelectedIndex(0);
        if (cboGiangVien.getItemCount() > 0) cboGiangVien.setSelectedIndex(0);
        if (cboPhongHoc.getItemCount() > 0) cboPhongHoc.setSelectedIndex(0);
        if (cboCaHoc.getItemCount() > 0) cboCaHoc.setSelectedIndex(0);
        txtNgayKG.setText("2026-12-01");
        txtNgayKT.setText("2027-02-28");
        txtSiSoMax.setText("15");
        cboTrangThai.setSelectedIndex(0);
        txtTimKiem.setText("");
        table.clearSelection();
    }
}
