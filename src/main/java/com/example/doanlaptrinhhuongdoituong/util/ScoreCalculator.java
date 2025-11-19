package com.example.doanlaptrinhhuongdoituong.util;

public class ScoreCalculator {

    // Lớp con để chứa kết quả
    public static class ScoreResult {
        public final float diemTK; // Thêm điểm TK (hệ 10) vào kết quả
        public final float diemHe4;
        public final String diemChu;
        public final String trangThai;

        public ScoreResult(float diemTK, float diemHe4, String diemChu, String trangThai) {
            this.diemTK = diemTK;
            this.diemHe4 = diemHe4;
            this.diemChu = diemChu;
            this.trangThai = trangThai;
        }
    }

    /**
     * Tính điểm (ĐÃ CẬP NHẬT CÔNG THỨC)
     * Công thức mới: diemTK = (diemQT + diemKT) / 2
     */
    public static ScoreResult calculate(float diemQT, float diemKT) {
        if (diemQT < 0 || diemQT > 10 || diemKT < 0 || diemKT > 10) {
            return new ScoreResult(0.0f, 0.0f, "F", "Không hợp lệ");
        }

        // *** ĐÂY LÀ THAY ĐỔI THEO YÊU CẦU CỦA BẠN ***
        double diemTK = (diemQT + diemKT) / 2.0;

        // Logic tính điểm (chuyển đổi từ diemTK sang hệ 4/chữ)
        if (diemTK < 4.0) {
            return new ScoreResult((float) diemTK, 0.0f, "F", "Trượt");
        } else if (diemTK < 5.0) {
            return new ScoreResult((float) diemTK, 1.0f, "D", "Đạt");
        } else if (diemTK < 5.5) {
            return new ScoreResult((float) diemTK, 1.5f, "D+", "Đạt");
        } else if (diemTK < 6.5) {
            return new ScoreResult((float) diemTK, 2.0f, "C", "Đạt");
        } else if (diemTK < 7.0) {
            return new ScoreResult((float) diemTK, 2.5f, "C+", "Đạt");
        } else if (diemTK < 8.0) {
            return new ScoreResult((float) diemTK, 3.0f, "B", "Đạt");
        } else if (diemTK < 8.5) {
            return new ScoreResult((float) diemTK, 3.5f, "B+", "Đạt");
        } else if (diemTK < 9.5) {
            return new ScoreResult((float) diemTK, 3.7f, "A", "Đạt"); // Giả sử
        } else {
            return new ScoreResult((float) diemTK, 4.0f, "A+", "Đạt");
        }
    }
}