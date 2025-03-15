package vn.hoidanit.jobhunter.service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class gobalException {
    @ExceptionHandler(value = idInvalidException.class)
    public ResponseEntity<String> handleIdException(idInvalidException idExcpection) {
        return ResponseEntity.badRequest().body(idExcpection.getMessage());
    }

}
