package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouensis.server.identity.security.captcha.InvalidCaptchaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mouensis.server.identity.security.AuthenticationFailureStatus.*;

/**
 * API 认证成功处理实现类型
 *
 * @author zhuyuan
 * @date 2020/12/1 14:08
 */
@Slf4j
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper;

    public LoginAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("validate error", exception);
        AuthenticationFailureResponse errorResponse = new AuthenticationFailureResponse(request.getRequestURI());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        if (exception instanceof InvalidCaptchaException) {
            errorResponse.setCode(INVALID_CAPTCHA.status());
        } else if (exception instanceof UsernameNotFoundException
                || exception instanceof BadCredentialsException) {
            errorResponse.setCode(INVALID_USERNAME_OR_PASSWORD.status());
        } else if (exception instanceof AccountExpiredException) {
            errorResponse.setCode(ACCOUNT_EXPIRED.status());
        } else if (exception instanceof LockedException) {
            errorResponse.setCode(ACCOUNT_LOCKED.status());
        } else if (exception instanceof DisabledException) {
            errorResponse.setCode(ACCOUNT_DISABLED.status());
        } else if (exception instanceof CredentialsExpiredException) {
            errorResponse.setCode(CREDENTIALS_EXPIRED.status());
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorResponse.setCode(FAILURE.status());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            errorResponse.setCode(FAILURE.status());
        }
        byte[] errorResponseBytes = objectMapper.writeValueAsBytes(errorResponse);
        response.setContentLength(errorResponseBytes.length);
        response.getWriter().print(new String(errorResponseBytes));
    }
}
