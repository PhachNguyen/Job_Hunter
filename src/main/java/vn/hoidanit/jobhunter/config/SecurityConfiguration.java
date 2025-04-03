package vn.hoidanit.jobhunter.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import vn.hoidanit.jobhunter.util.SecurityUtil;
// Giúp bảo vệ API bằng token

@Configuration
@EnableMethodSecurity(securedEnabled = true) // Bảo mật
public class SecurityConfiguration {
    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtKey;

    // @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    // private String jwtKeyExpriration;

    // Mã hóa password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng thuật toán BCrypt để hashpass
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, // Cấu hình security của Spring Security
            customAuthenticationEntrypoint customAuthenticationEntrypoint) throws Exception {
        http
                .csrf(c -> c.disable()) // Do đang dùng JWT nên k áp dụng trong mô hình Sateless
                .cors(Customizer.withDefaults()) // Giup Spring Security sử dụng config đã định nghĩa trong CorsConfig
                                                 // để FE React gọi API
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/", "/login", "/api/v1/login","/api/v1/auth/login",
                                        "/api/v1/auth/refresh" )
                                .permitAll() // Cho phép k
                                // cần phải
                                // đăng nhập
                                .anyRequest().authenticated()
                // .anyRequest().permitAll())
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()) // Bật xác thực JWT
                        .authenticationEntryPoint(customAuthenticationEntrypoint))
                // default exception
                // .exceptionHandling(
                // exceptions -> exceptions
                // .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
                // .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) // 403
                // custom

                .formLogin(f -> f.disable()) // Tắt login của Spring security
                // Chuyển sang mô hình STATELESS vì mặc định cơ chế session là STATEFUL
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Chuyển
                                                                                                               // sang
                                                                                                               // mô
                                                                                                               // hình
                                                                                                               // StateLess
                                                                                                               // để k
                                                                                                               // lưu
                                                                                                               // session
        return http.build();

    }

    // @FunctionalInterface
    // Decode JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

// Mã hóa JWT
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                SecurityUtil.JWT_ALGORITHM.getName());
    }

    // khi decode thành công
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("Permission"); // Quyền hạn
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
