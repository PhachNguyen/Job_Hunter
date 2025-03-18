package vn.hoidanit.jobhunter.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

// Lấy tham số môi trường 
@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public void createToken(Authentication authentication) {

    }
}
