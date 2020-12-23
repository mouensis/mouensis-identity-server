package com.mouensis.server.identity.security.captcha;

/**
 * 登录验证码生成器
 *
 * @author zhuyuan
 * @date 2020/12/16 21:16
 */
public interface CaptchaGenerator {

    /**
     * 生成验证码
     *
     * @return 验证码字节组
     */
    LoginCaptcha generate();
}
