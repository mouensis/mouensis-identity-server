package com.mouensis.server.identity.security.captcha;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常
 *
 * @author zhuyuan
 * @date 2020/12/16 17:17
 */
public class InvalidCaptchaException extends AuthenticationException {
    public InvalidCaptchaException(String msg) {
        super(msg);
    }
}
