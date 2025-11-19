package com.example.doanlaptrinhhuongdoituong.dao.Teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienDiem;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherStaticDAO {

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

    //
    // =========================================================================
    // *** PHẦN SỬA LỖI NẰM TẠI ĐÂY (HÀM getDiemSinhVien) ***
    //
    // Đã thay thế 'NULLS LAST' bằng cú pháp ISNULL() của MySQL
    // =========================================================================
    public ObservableList<SinhVienDiem> getDiemSinhVien(String maLopHP, String sortOrder) {
        ObservableList<SinhVienDiem> dsDiem = FXCollections.observableArrayList();

        // Xây dựng câu lệnh ORDER BY (Tương thích với MySQL)
        String sortClause;
        if ("ASC".equals(sortOrder)) {
            // Tăng dần: Sắp xếp NULL ở cuối, sau đó sắp xếp điểm tăng dần
            // ISNULL(col) trả về 1 nếu là NULL, 0 nếu không.
            sortClause = "ORDER BY ISNULL(dkk.diemHe4) ASC, dkk.diemHe4 ASC, sv.hoTen";
        } else if ("DESC".equals(sortOrder)) {
            // Giảm dần: Sắp xếp NULL ở cuối, sau đó sắp xếp điểm giảm dần
            sortClause = "ORDER BY ISNULL(dkk.diemHe4) ASC, dkk.diemHe4 DESC, sv.hoTen";
        } else {
            // Mặc định sắp xếp theo tên
            sortClause = "ORDER BY sv.hoTen";
        }

        String query = "SELECT sv.maSV, sv.hoTen, dkk.diemQuaTrinh, dkk.diemKetThuc, dkk.diemHe4, dkk.diemChu, dkk.trangThaiHoc " +
                "FROM dangki_ketqua dkk " +
                "JOIN sinhvien sv ON dkk.maSV = sv.maSV " +
                "WHERE dkk.maLopHP = ? " +
                sortClause; // Thêm mệnh đề sắp xếp đã sửa

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maLopHP);

            ResultSet rs = pstmt.executeQuery();
            int stt = 1;
            while (rs.next()) {
                Float diemQT = (Float) rs.getObject("diemQuaTrinh");
                Float diemKT = (Float) rs.getObject("diemKetThuc");

                // Tính toán diemTK (hệ 10)
                Float diemTK = null;
                if (diemQT != null && diemKT != null) {
                    diemTK = (diemQT + diemKT) / 2.0f;
                }

                dsDiem.add(new SinhVienDiem(
                        stt++,
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        diemQT,
                        diemKT,
                        diemTK, // Điểm TK (hệ 10)
                        (Float) rs.getObject("diemHe4"),
                        rs.getString("diemChu"),
                        rs.getString("trangThaiHoc")
                ));
            }
        } catch (Exception e) {
            // In ra lỗi SQL nếu có
            e.printStackTrace();
        }
        return dsDiem;
    }
}