package vn.hoidanit.jobhunter.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.restResponse;

// Đánh dấu đây là một Global Exception Handler.
// Bắt lỗi (Exception Handling) toàn cục trong ứng dụng.
// Tạo phản hồi JSON thống nhất khi xảy ra lỗi.
// Giúp mã nguồn gọn gàng vì không cần try-catch ở nhiều nơi.
@RestControllerAdvice
public class gobalException {
    // Chỉ định danh sách các Exception mà phương thức sẽ xử lý.
    @ExceptionHandler(value = {
            idInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<restResponse<Object>> handleIdException(Exception ex) {
        restResponse<Object> res = new restResponse<Object>(); // Tạo một đối tượng phản hồi lỗi
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Execption occurs");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
