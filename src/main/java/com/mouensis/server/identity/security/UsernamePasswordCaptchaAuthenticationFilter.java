package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouensis.framework.web.filter.ContentCachingRequestWrapper;
import com.mouensis.server.identity.security.captcha.InvalidCaptchaException;
import com.mouensis.server.identity.security.captcha.LoginCaptchaValidator;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义用户名密码API获取token值认证
 *
 * @author zhuyuan
 * @date 2020/11/30 23:19
 */
public final class UsernamePasswordCaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/token", "POST");

    private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;
    /**
     * 默认开始验证码校验
     */
    private boolean enableCaptcha = true;

    private final ObjectMapper objectMapper;
    private final LoginCaptchaValidator loginCaptchaValidator;

    public UsernamePasswordCaptchaAuthenticationFilter(ObjectMapper objectMapper, LoginCaptchaValidator loginCaptchaValidator) {
        super.setRequiresAuthenticationRequestMatcher(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        super.setAuthenticationManager(super.getAuthenticationManager());
        this.objectMapper = objectMapper;
        this.loginCaptchaValidator = loginCaptchaValidator;
        this.setAuthenticationFailureHandler(new LoginAuthenticationFailureHandler(this.objectMapper));
        this.setAuthenticationSuccessHandler(new LoginAuthenticationSuccessHandler(this.objectMapper));
    }

    public void setEnableCaptcha(boolean enableCaptcha) {
        this.enableCaptcha = enableCaptcha;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        if (enableCaptcha) {
            try {
//                this.loginCaptchaValidator.validate(request, obtainCaptcha(request));
                super.doFilter(request, response, chain);
            } catch (InvalidCaptchaException failed) {
                unsuccessfulAuthentication(request, response, failed);
            }
        } else {
            super.doFilter(request, response, chain);
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    protected String obtainCaptcha(HttpServletRequest request) {
        if (isContentCachingRequestWrapper(request)) {
            try {
                byte[] body = getContentCachingRequestWrapper(request).getBody();
                JsonNode requestBody = objectMapper.readValue(body, JsonNode.class);
                if (requestBody != null && requestBody.has(getCaptchaParameter())) {
                    return requestBody.get(getCaptchaParameter()).asText();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read password from cached content", e);
            }
        }

        return super.obtainPassword(request);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        if (isContentCachingRequestWrapper(request)) {
            try {
                byte[] body = getContentCachingRequestWrapper(request).getBody();
                JsonNode requestBody = objectMapper.readValue(body, JsonNode.class);
                if (requestBody != null && requestBody.has(getPasswordParameter())) {
                    return requestBody.get(getPasswordParameter()).asText();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read password from cached content", e);
            }
        }

        return super.obtainPassword(request);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        if (isContentCachingRequestWrapper(request)) {
            try {
                byte[] body = getContentCachingRequestWrapper(request).getBody();
                JsonNode requestBody = objectMapper.readValue(body, JsonNode.class);
                if (requestBody != null && requestBody.has(getUsernameParameter())) {
                    return requestBody.get(getUsernameParameter()).asText();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read username from cached content", e);
            }
        }

        return super.obtainPassword(request);
    }

    private ContentCachingRequestWrapper getContentCachingRequestWrapper(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else if (request instanceof HttpServletRequestWrapper) {
            return getContentCachingRequestWrapper((HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest());
        }
        return null;
    }

    private boolean isContentCachingRequestWrapper(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return true;
        } else if (request instanceof HttpServletRequestWrapper) {
            return isContentCachingRequestWrapper((HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest());
        }
        return false;
    }

    public final String getCaptchaParameter() {
        return this.captchaParameter;
    }

}
