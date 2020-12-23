package com.mouensis.server.identity.security.captcha;

import java.time.Duration;

/**
 * 验证码token值存储服务接口
 *
 * @author zhuyuan
 * @date 2020/12/20 18:53
 */
public interface CaptchaTokenStoreService {
    /**
     * 存储验证码临时token和验证码文本
     *
     * @param key
     * @param value
     * @param timeout
     */
    void store(String key, String value, Duration timeout);
}
