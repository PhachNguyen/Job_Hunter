package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Kết quả trả về từ API
public class resultPaginationDTO {
    private meta meta; // Thông tin phân trang
    private Object result; // Danh sách dữ liệu (Dạng Generic)
}
