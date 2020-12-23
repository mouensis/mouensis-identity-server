package com.mouensis.server.identity.security.captcha;

import org.springframework.core.log.LogMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码认证过滤器
 *
 * @author zhuyuan
 * @date 2020/12/20 19:22
 */
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";

    private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;


    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login",
            "POST");
    /**
     * 默认开始验证码校验
     */
    private boolean enableCaptcha = true;

    private final CaptchaValidator captchaValidator;
    private final AuthenticationFailureHandler failureHandler;

    public CaptchaAuthenticationFilter(CaptchaValidator captchaValidator, AuthenticationFailureHandler failureHandler) {
        super();
        this.captchaValidator = captchaValidator;
        this.failureHandler = failureHandler;
    }

    public void setEnableCaptcha(boolean enableCaptcha) {
        this.enableCaptcha = enableCaptcha;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (enableCaptcha) {
            try {
                this.captchaValidator.validate(request, obtainCaptcha(request));
                super.doFilter(request, response, filterChain);
            } catch (InvalidCaptchaException failed) {
                unsuccessfulAuthentication(request, response, failed);
            }
        } else {
            super.doFilter(request, response, filterChain);
        }
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            InvalidCaptchaException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
            return true;
        }
        if (this.logger.isTraceEnabled()) {
            this.logger
                    .trace(LogMessage.format("Did not match request to %s", DEFAULT_ANT_PATH_REQUEST_MATCHER));
        }
        return false;
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(this.captchaParameter);
    }


    public final String getCaptchaParameter() {
        return this.captchaParameter;
    }

}
