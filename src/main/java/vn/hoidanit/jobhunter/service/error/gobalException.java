package vn.hoidanit.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.restResponse;

// Exception tổng
@RestControllerAdvice
public class gobalException {
    @ExceptionHandler(value = {
            // idInvalidException.class
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<restResponse<Object>> handleIdException(Exception ex) {
        restResponse<Object> res = new restResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Execption occurs");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
