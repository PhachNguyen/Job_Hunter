package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import vn.hoidanit.jobhunter.controller.AuthController;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.restResponse;

// Dùng @RestControllerAdvice or ControllerAdvice
@ControllerAdvice
public class fomartRestResponse implements ResponseBodyAdvice {

    private final AuthController authController;

    fomartRestResponse(AuthController authController) {
        this.authController = authController;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        // Convert sang ServerletServer
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        restResponse<Object> res = new restResponse<Object>();
        res.setStatusCode(status);
        if (body instanceof String) {
            return body;
        }
        // case error (200-399 is succes)
        if (status >= 400) {
            res.setError("CALL API FAILED");
            res.setMessage(body);
        }
        // case succes
        else {
            res.setData(body);
            res.setMessage("GET API SUCCESS");
        }
        // TODO Auto-generated method stub
        return res;

    }
}
