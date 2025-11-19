package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import com.example.doanlaptrinhhuongdoituong.dao.Teacher.TeacherAddScoreDAO;
import com.example.doanlaptrinhhuongdoituong.util.ScoreCalculator;
import javafx.collections.ObservableList;

public class TeacherAddScoreModel {

    private final TeacherAddScoreDAO dao;

    public TeacherAddScoreModel() {
        this.dao = new TeacherAddScoreDAO();
    }

    public ObservableList<LopDayHoc> getLopGiangDay(String maGV) {
        return dao.getLopGiangDay(maGV);
    }

    public ObservableList<SinhVienDiem> getDiemSinhVien(String maLopHP, String searchText) {
        return dao.getDiemSinhVien(maLopHP, searchText);
    }

    /**
     * Cập nhật điểm và trả về kết quả đã tính toán
     */
    public ScoreCalculator.ScoreResult updateDiem(String maSV, String maLopHP, float diemQT, float diemKT) {
        // Gọi DAO để cập nhật DB
        boolean success = dao.updateDiem(maSV, maLopHP, diemQT, diemKT);

        if (success) {
            // Nếu DB cập nhật thành công, tính toán lại và trả về kết quả
            // để Controller cập nhật giao diện
            // ScoreCalculator.calculate sẽ trả về 4 giá trị (TK, He4, Chu, TrangThai)
            return ScoreCalculator.calculate(diemQT, diemKT);
        } else {
            return null; // Trả về null nếu lỗi
        }
    }
}