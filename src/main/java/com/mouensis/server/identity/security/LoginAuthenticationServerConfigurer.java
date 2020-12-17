package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouensis.server.identity.security.captcha.LoginCaptchaEndpointFilter;
import com.mouensis.server.identity.security.captcha.LoginCaptchaRedisValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @author zhuyuan
 */
public final class LoginAuthenticationServerConfigurer<B extends HttpSecurityBuilder<B>>
        extends AbstractHttpConfigurer<LoginAuthenticationServerConfigurer<B>, B> {
    @Override
    public void configure(B builder) {
        LoginCaptchaEndpointFilter loginCaptchaEndpointFilter = new LoginCaptchaEndpointFilter(
                getStringRedisTemplate(builder));
        builder.addFilterBefore(postProcess(loginCaptchaEndpointFilter), AbstractPreAuthenticatedProcessingFilter.class);

        UsernamePasswordCaptchaAuthenticationFilter usernamePasswordCaptchaAuthenticationFilter =
                new UsernamePasswordCaptchaAuthenticationFilter(getObjectMapper(builder),
                        new LoginCaptchaRedisValidator(getStringRedisTemplate(builder)));
        usernamePasswordCaptchaAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        builder.addFilterBefore(usernamePasswordCaptchaAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);

    }

    private static <B extends HttpSecurityBuilder<B>> ObjectMapper getObjectMapper(B builder) {
        ObjectMapper objectMapper = builder.getSharedObject(ObjectMapper.class);
        if (objectMapper == null) {
            objectMapper = getObjectMapperBean(builder);
            builder.setSharedObject(ObjectMapper.class, objectMapper);
        }
        return objectMapper;
    }

    private static <B extends HttpSecurityBuilder<B>> ObjectMapper getObjectMapperBean(B builder) {
        return builder.getSharedObject(ApplicationContext.class).getBean(ObjectMapper.class);
    }

    private static <B extends HttpSecurityBuilder<B>> StringRedisTemplate getStringRedisTemplate(B builder) {
        StringRedisTemplate stringRedisTemplate = builder.getSharedObject(StringRedisTemplate.class);
        if (stringRedisTemplate == null) {
            stringRedisTemplate = getStringRedisTemplateBean(builder);
            builder.setSharedObject(StringRedisTemplate.class, stringRedisTemplate);
        }
        return stringRedisTemplate;
    }

    private static <B extends HttpSecurityBuilder<B>> StringRedisTemplate getStringRedisTemplateBean(B builder) {
        return builder.getSharedObject(ApplicationContext.class).getBean(StringRedisTemplate.class);
    }
}