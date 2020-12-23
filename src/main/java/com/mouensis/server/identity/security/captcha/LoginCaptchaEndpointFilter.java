package com.mouensis.server.identity.security.captcha;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * 登录验证码端点过滤器
 *
 * @author zhuyuan
 * @date 2020/12/16 11:00
 */
public class LoginCaptchaEndpointFilter extends OncePerRequestFilter {
    /**
     * The default endpoint {@code URI} for captcha requests.
     */
    public static final String DEFAULT_CAPTCHA_ENDPOINT_URI = "/captcha";
    public static final String CAPTCHA_CODE_CACHED_KEY_PREFIX = "CAPTCHA_";
    public static final String HEADER_CAPTCHA_TOKEN_KEY = "Captcha-Token";
    private RequestMatcher captchaEndpointMatcher;
    private CaptchaGenerator captchaGenerator;
    private CaptchaTokenStoreService captchaTokenStoreService;

    public LoginCaptchaEndpointFilter(CaptchaTokenStoreService captchaTokenStoreService) {
        this(new DefaultCaptchaGenerator(), captchaTokenStoreService);
    }

    public LoginCaptchaEndpointFilter(CaptchaGenerator captchaGenerator, CaptchaTokenStoreService captchaTokenStoreService) {
        this.captchaEndpointMatcher = new AntPathRequestMatcher(DEFAULT_CAPTCHA_ENDPOINT_URI, HttpMethod.GET.name());
        this.captchaGenerator = captchaGenerator;
        this.captchaTokenStoreService = captchaTokenStoreService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!this.captchaEndpointMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        //生成验证码
        LoginCaptcha loginCaptcha = this.captchaGenerator.generate();
        //验证码零时token值，丢入redis缓存，校验时反查使用
        String captchaToken = CAPTCHA_CODE_CACHED_KEY_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");
        captchaTokenStoreService.store(captchaToken, loginCaptcha.getCode(), Duration.ofMinutes(2));
        //输出验证码
        sendLoginCaptchaResponse(response, captchaToken, loginCaptcha.getImage());
    }

    private void sendLoginCaptchaResponse(HttpServletResponse response, String captchaToken, byte[] image) throws IOException {
        response.setHeader(HEADER_CAPTCHA_TOKEN_KEY, captchaToken);
        //设置响应头
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        //设置响应头
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        //在代理服务器端防止缓冲
        response.setDateHeader(HttpHeaders.EXPIRES, 0);
        //设置响应内容类型
        response.setContentType("image/jpeg");
        response.getOutputStream().write(image);
        response.getOutputStream().flush();
    }

    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    public void setCaptchaTokenStoreService(CaptchaTokenStoreService captchaTokenStoreService) {
        this.captchaTokenStoreService = captchaTokenStoreService;
    }
}
