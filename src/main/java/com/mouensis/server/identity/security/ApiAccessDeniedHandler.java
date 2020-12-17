package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问拒绝处理实现类
 *
 * @author zhuyuan
 * @date 2020/12/1 13:50
 */
class ApiAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper;

    public ApiAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AuthenticationFailureResponse errorResponse = AuthenticationFailureResponse.error(request.getRequestURI(), String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        byte[] errorResponseBytes = objectMapper.writeValueAsBytes(errorResponse);
        response.setContentLength(errorResponseBytes.length);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().print(new String(errorResponseBytes));
    }
}
