package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Hàm resLoginDTO định dạng dữ liệu khi user login success
@Getter
@Setter
public class ResLoginDTO {
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    // Gán thành static
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
    }

}
