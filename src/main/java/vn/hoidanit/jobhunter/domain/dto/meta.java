package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class meta {
    // Chứa thông tin về phân trang 
    private int page;
    private int pageSize;
    private int pages; // Tổng số trang
    private long total; // Tổng số phần tử 
}
