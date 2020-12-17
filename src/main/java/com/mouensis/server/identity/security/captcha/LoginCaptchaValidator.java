package com.mouensis.server.identity.security.captcha;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码校验
 *
 * @author zhuyuan
 * @date 2020/12/16 21:06
 */
public interface LoginCaptchaValidator {

    /**
     * 验证码校验
     *
     * @param request
     * @param captcha
     * @return
     */
    boolean validate(HttpServletRequest request, String captcha);
}
