package com.mouensis.server.identity.security.captcha;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * Redis存储
 *
 * @author zhuyuan
 * @date 2020/12/20 19:17
 */
public class RedisCaptchaTokenStoreService implements CaptchaTokenStoreService {

    private StringRedisTemplate stringRedisTemplate;

    public RedisCaptchaTokenStoreService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void store(String key, String value, Duration timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout);
    }
}
