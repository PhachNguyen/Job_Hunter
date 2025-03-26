package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
