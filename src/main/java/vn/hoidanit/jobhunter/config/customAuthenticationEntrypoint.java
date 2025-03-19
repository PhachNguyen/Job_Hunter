package vn.hoidanit.jobhunter.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.restResponse;

@Component
public class customAuthenticationEntrypoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public customAuthenticationEntrypoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");

        restResponse<Object> res = new restResponse<Object>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        String errorMessage = Optional.ofNullable(authException.getCause()) // NULL
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());

        res.setError(authException.getCause().getMessage());
        res.setMessage(
                "Token is invalid (expired, not in the correct format, or does not transmit JWT in the header)...");

        mapper.writeValue(response.getWriter(), res);
    }

}
