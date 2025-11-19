package com.example.doanlaptrinhhuongdoituong.model.Teacher;

// Lớp này dùng để chứa dữ liệu cho ComboBox chọn lớp
public class LopDayHoc {
    private String maLopHP;
    private String tenMonHoc;
    private String tenLopHP; // Ví dụ: "Lớp Nhập môn lập trình (CQ 01)"

    public LopDayHoc(String maLopHP, String tenMonHoc, String tenLopHP) {
        this.maLopHP = maLopHP;
        this.tenMonHoc = tenMonHoc;
        this.tenLopHP = (tenLopHP != null && !tenLopHP.isEmpty()) ? tenLopHP : tenMonHoc;
    }

    public String getMaLopHP() {
        return maLopHP;
    }

    // Hàm toString() này rất quan trọng
    // Nó quyết định ComboBox sẽ hiển thị cái gì
    @Override
    public String toString() {
        return this.tenLopHP; // Hiển thị tên đầy đủ của lớp
    }
}