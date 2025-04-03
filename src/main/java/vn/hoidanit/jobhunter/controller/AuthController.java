package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.userService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.idInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil SercurityUitl;
    private final userService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil SercurityUitl,
                          userService userService) {
        this.SercurityUitl = SercurityUitl;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpriration;

    // Hàm tạo Cookies
    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Tạo Token

        // khi login
        // Authentication authentication =
        // authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        Set thông tin người dùng đăng nhập vào Context (Có thể sử dụng sau này ) nhận diện người dùng hiện tại
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();
        User currentUser = this.userService.handlerGetUserbyUserName(loginDTO.getUsername());
        if (currentUser != null) {
//            ResLoginDTO.UserLogin userLogin = res.new UserLogin Không gán static
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUser.getId(),
                    currentUser.getEmail(),
                    currentUser.getName());
            res.setUser(userLogin);
        }
        String access_token = this.SercurityUitl.createAccessToken(authentication.getName(), res.getUser());

        res.setAccessToken(access_token);

        // create Refresh Token
        String refreshToken = this.SercurityUitl.createRefreshToken(loginDTO.getUsername(), res);
        // Update user
        this.userService.updateUserToken(refreshToken, loginDTO.getUsername());
        // Set Cookies :
        ResponseCookie responseCookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpriration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res); // Trả về đối tượng ResLoginDTO
    }

    // Dùng để lấy user hiện tại dựa vào JWT đăng nhập
    // Get API account
    @GetMapping("/auth/account")
    @ApiMessage("Fetch account message")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
//          Toán tử 3 ngôi
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.handlerGetUserbyUserName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setName(currentUser.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    // Hàm refresh Token lấy cookies
    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token ")
    public ResponseEntity<ResLoginDTO> refreshToken(
            @CookieValue(value = "refreshToken", defaultValue = "Nothing") String refreshToken
    ) throws idInvalidException {
        if(refreshToken.equals("Nothing")){
            throw new idInvalidException("You don't have refresh token. Please login again.");
        }
//        Check Valid
        Jwt decodeToken = this.SercurityUitl.checkValidRefreshToken(refreshToken);
        String email = decodeToken.getSubject(); // decodeToken
        // Hàm check user by token + email 
        User currentUserCheck = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUserCheck == null) {
            throw new idInvalidException("Refresh token is invalid or expired. Please login again.");
        } else {
            ResLoginDTO res = new ResLoginDTO();
        User currentUser = this.userService.handlerGetUserbyUserName(email);
            if (currentUser != null) {
//            ResLoginDTO.UserLogin userLogin = res.new UserLogin Không gán static
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                        currentUser.getId(),
                        currentUser.getEmail(),
                        currentUser.getName());
                res.setUser(userLogin);
            }
//            create access token
            String access_token = this.SercurityUitl.createAccessToken(email, res.getUser());

            res.setAccessToken(access_token);

            // Create new Refresh Token
            String new_refreshToken = this.SercurityUitl.createRefreshToken(email, res);
            // Update user
            this.userService.updateUserToken(refreshToken, email);
            // Set Cookies :
            ResponseCookie responseCookie = ResponseCookie
                    .from("refreshToken", new_refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpriration)
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(res); // Trả về đối tượng ResLoginDTO
        }
    }
}

