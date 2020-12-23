package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouensis.server.identity.security.captcha.CaptchaAuthenticationFilter;
import com.mouensis.server.identity.security.captcha.LoginCaptchaEndpointFilter;
import com.mouensis.server.identity.security.captcha.RedisCaptchaTokenStoreService;
import com.mouensis.server.identity.security.captcha.RedisCaptchaValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @author zhuyuan
 */
public final class LoginAuthenticationServerConfigurer<B extends HttpSecurityBuilder<B>>
        extends AbstractHttpConfigurer<LoginAuthenticationServerConfigurer<B>, B> {
    @Override
    public void configure(B builder) {
        LoginCaptchaEndpointFilter loginCaptchaEndpointFilter = new LoginCaptchaEndpointFilter(
                new RedisCaptchaTokenStoreService(getStringRedisTemplate(builder)));
        builder.addFilterBefore(postProcess(loginCaptchaEndpointFilter), AbstractPreAuthenticatedProcessingFilter.class);

//        CaptchaAuthenticationFilter captchaAuthenticationFilter =
//                new CaptchaAuthenticationFilter(new RedisCaptchaValidator(getStringRedisTemplate(builder)),
//                        new LoginAuthenticationFailureHandler(getObjectMapper(builder)));
//
//        builder.addFilterBefore(captchaAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);

//        WebTokenAuthenticationFilter webTokenAuthenticationFilter =
//                new WebTokenAuthenticationFilter(getObjectMapper(builder), builder.getSharedObject(AuthenticationManager.class));
//        builder.addFilterAfter(webTokenAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);

        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter
                .setAuthenticationFailureHandler(new LoginAuthenticationFailureHandler(getObjectMapper(builder)));
        usernamePasswordAuthenticationFilter
                .setAuthenticationSuccessHandler(
                        new LoginAuthenticationSuccessHandler(getObjectMapper(builder),
                                getOAuth2AuthorizedClientManager(builder)));
        usernamePasswordAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        builder.addFilterBefore(usernamePasswordAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    private OAuth2AuthorizedClientManager getOAuth2AuthorizedClientManager(B builder) {
        OAuth2AuthorizedClientManager auth2AuthorizedClientManager = builder.getSharedObject(OAuth2AuthorizedClientManager.class);
        if (auth2AuthorizedClientManager == null) {
            auth2AuthorizedClientManager = getOAuth2AuthorizedClientManagerBean(builder);
            builder.setSharedObject(OAuth2AuthorizedClientManager.class, auth2AuthorizedClientManager);
        }
        return auth2AuthorizedClientManager;
    }

    private OAuth2AuthorizedClientManager getOAuth2AuthorizedClientManagerBean(B builder) {
        return builder.getSharedObject(ApplicationContext.class).getBean(OAuth2AuthorizedClientManager.class);
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