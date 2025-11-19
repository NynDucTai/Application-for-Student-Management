package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import com.example.doanlaptrinhhuongdoituong.dao.Teacher.TeacherStaticDAO;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;

public class TeacherStaticModel {

    private final TeacherStaticDAO dao;

    public TeacherStaticModel() {
        this.dao = new TeacherStaticDAO();
    }

    public ObservableList<LopDayHoc> getLopGiangDay(String maGV) {
        return dao.getLopGiangDay(maGV);
    }

    public ObservableList<SinhVienDiem> getDiemSinhVien(String maLopHP, String sortOrder) {
        return dao.getDiemSinhVien(maLopHP, sortOrder);
    }

    /**
     * Tính toán thống kê (ĐÃ CẢI TIẾN LOGIC SO SÁNH)
     */
    public Map<String, Integer> calculateStatistics(ObservableList<SinhVienDiem> studentList) {
        Map<String, Integer> stats = new HashMap<>();
        int quaMon = 0, truotMon = 0, datA = 0, datB = 0, datC = 0, datD = 0;

        if (studentList == null) {
            return stats;
        }

        for (SinhVienDiem sv : studentList) {
            // 1. Thống kê Trạng thái (Dùng trim() và equalsIgnoreCase để chính xác hơn)
            String trangThai = sv.getTrangThai();
            if (trangThai != null) {
                String tt = trangThai.trim(); // Xóa khoảng trắng thừa
                if (tt.equalsIgnoreCase("Đạt")) {
                    quaMon++;
                } else if (tt.equalsIgnoreCase("Trượt") || tt.equalsIgnoreCase("Học lại")) {
                    truotMon++;
                }
            }

            // 2. Thống kê Điểm chữ (Dùng trim() và equalsIgnoreCase)
            String diemChu = sv.getDiemChu();
            if (diemChu != null) {
                String dc = diemChu.trim().toUpperCase(); // Chuyển thành chữ hoa và xóa khoảng trắng

                if (dc.equals("A") || dc.equals("A+")) {
                    datA++;
                } else if (dc.equals("B") || dc.equals("B+")) {
                    datB++;
                } else if (dc.equals("C") || dc.equals("C+")) {
                    datC++;
                } else if (dc.equals("D") || dc.equals("D+")) {
                    datD++;
                }
            }
        }

        stats.put("quaMon", quaMon);
        stats.put("truotMon", truotMon);
        stats.put("datA", datA);
        stats.put("datB", datB);
        stats.put("datC", datC);
        stats.put("datD", datD);

        return stats;
    }
}