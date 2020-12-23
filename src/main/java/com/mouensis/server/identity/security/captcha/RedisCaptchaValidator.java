package com.mouensis.server.identity.security.captcha;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码校验 查询Redis实现
 *
 * @author zhuyuan
 * @date 2020/12/16 21:07
 */
@Slf4j
public class RedisCaptchaValidator implements CaptchaValidator {

    private StringRedisTemplate stringRedisTemplate;

    public RedisCaptchaValidator(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean validate(HttpServletRequest request, String captcha) {
        String captchaToken = request.getHeader(LoginCaptchaEndpointFilter.HEADER_CAPTCHA_TOKEN_KEY.toLowerCase());
        //传递的验证码，
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(captchaToken)) {
            log.debug("Failed to authenticate since captcha is blank or captchaToken is blank");
            throw new InvalidCaptchaException("Captcha is Blank or CaptchaToken is Blank");
        }
        //取出缓存的验证码
        String cachedCaptcha = this.stringRedisTemplate.opsForValue().get(captchaToken);
        //取出直接删除，只能验证一次
        if(StringUtils.isNotBlank(cachedCaptcha)){
            this.stringRedisTemplate.delete(captchaToken);
        }
        if (!captcha.equalsIgnoreCase(cachedCaptcha)) {
            log.debug("Failed to authenticate since captcha is invalid");
            throw new InvalidCaptchaException("Captcha is invalid");
        }
        return true;
    }
}
