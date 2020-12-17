package com.mouensis.server.identity.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
class ApiAccessDeniedEntryPoint implements AuthenticationEntryPoint {

    private AccessDeniedHandler accessDeniedHandler;

    public ApiAccessDeniedEntryPoint(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Always returns a 403 error code to the client.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        accessDeniedHandler.handle(request, response, new AccessDeniedException(""));
    }
}