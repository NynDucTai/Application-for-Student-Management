package com.example.doanlaptrinhhuongdoituong.dao.Teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienDiem;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import com.example.doanlaptrinhhuongdoituong.util.ScoreCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherAddScoreDAO {

    // 1. Lấy lớp giảng dạy (Giữ nguyên)
    public ObservableList<LopDayHoc> getLopGiangDay(String maGV) {
        ObservableList<LopDayHoc> dsLop = FXCollections.observableArrayList();
        String query = "SELECT lhp.maLopHP, mh.tenMon, lhp.tenLopHP " +
                "FROM phanconggiangday pcg " +
                "JOIN lophocphan lhp ON pcg.maLopHP = lhp.maLopHP " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE pcg.maGV = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maGV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dsLop.add(new LopDayHoc(rs.getString("maLopHP"), rs.getString("tenMon"), rs.getString("tenLopHP")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dsLop;
    }

    // 2. Lấy danh sách điểm của sinh viên (ĐÃ CẬP NHẬT)
    public ObservableList<SinhVienDiem> getDiemSinhVien(String maLopHP, String searchText) {
        ObservableList<SinhVienDiem> dsDiem = FXCollections.observableArrayList();
        String searchPattern = "%" + (searchText != null ? searchText : "") + "%";

        String query = "SELECT sv.maSV, sv.hoTen, dkk.diemQuaTrinh, dkk.diemKetThuc, dkk.diemHe4, dkk.diemChu, dkk.trangThaiHoc " +
                "FROM dangki_ketqua dkk " +
                "JOIN sinhvien sv ON dkk.maSV = sv.maSV " +
                "WHERE dkk.maLopHP = ? AND (sv.maSV LIKE ? OR sv.hoTen LIKE ?) " +
                "ORDER BY " +
                "  CASE WHEN (sv.maSV LIKE ? OR sv.hoTen LIKE ?) THEN 0 ELSE 1 END, " +
                "  sv.hoTen";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maLopHP);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            int stt = 1;
            while (rs.next()) {

                Float diemQT = (Float) rs.getObject("diemQuaTrinh");
                Float diemKT = (Float) rs.getObject("diemKetThuc");

                // --- TÍNH TOÁN diemTK (hệ 10) ---
                Float diemTK = null;
                if (diemQT != null && diemKT != null) {
                    // Áp dụng công thức (QT + KT) / 2
                    diemTK = (diemQT + diemKT) / 2.0f;
                }

                dsDiem.add(new SinhVienDiem(
                        stt++,
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        diemQT, // Điểm QT
                        diemKT, // Điểm KT
                        diemTK, // Điểm TK (hệ 10) [MỚI]
                        (Float) rs.getObject("diemHe4"), // Điểm hệ 4
                        rs.getString("diemChu"), // Điểm chữ
                        rs.getString("trangThaiHoc") // Trạng thái
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dsDiem;
    }

    // 3. Cập nhật điểm (Giữ nguyên - Vì nó dùng ScoreCalculator đã được sửa)
    public boolean updateDiem(String maSV, String maLopHP, float diemQT, float diemKT) {
        // Tự động dùng công thức mới vì ScoreCalculator đã được sửa
        ScoreCalculator.ScoreResult result = ScoreCalculator.calculate(diemQT, diemKT);

        // Database không có cột diemTK (hệ 10),
        // nên chúng ta chỉ lưu QT, KT, He4, Chu, TrangThai
        String query = "UPDATE dangki_ketqua " +
                "SET diemQuaTrinh = ?, diemKetThuc = ?, diemHe4 = ?, diemChu = ?, trangThaiHoc = ? " +
                "WHERE maSV = ? AND maLopHP = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setFloat(1, diemQT);
            pstmt.setFloat(2, diemKT);
            pstmt.setFloat(3, result.diemHe4);
            pstmt.setString(4, result.diemChu);
            pstmt.setString(5, result.trangThai);
            pstmt.setString(6, maSV);
            pstmt.setString(7, maLopHP);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}