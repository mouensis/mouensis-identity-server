package com.mouensis.server.identity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * API 认证成功处理实现类型
 *
 * @author zhuyuan
 * @date 2020/12/1 14:06
 */
@Slf4j
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private OAuth2AuthorizedClientManager authorizedClientManager;

    public LoginAuthenticationSuccessHandler(ObjectMapper objectMapper, OAuth2AuthorizedClientManager authorizedClientManager) {
        this.objectMapper = objectMapper;
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("web-identity")
                .principal(authentication)
                .attributes(attrs -> {
                    attrs.put(HttpServletRequest.class.getName(), new PasswordHttpServletRequestWrapper(request, (User) authentication.getPrincipal()));
                    attrs.put(HttpServletResponse.class.getName(), response);
                })
                .build();
        OAuth2AuthorizedClient authorizedClient = null;
        try {
            authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);
        } catch (ClientAuthorizationException cae) {
            log.info("Try to authenticate again ---------------------");
            try {
                authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);
            } catch (Exception e) {
                log.info("Authorization server failed to authorize", e);
                sendFailureResponse(request, response);
            }
        } catch (Exception e) {
            log.info("Authorization server failed to authorize", e);
            sendFailureResponse(request, response);
        }

        sendSuccessResponse(response, authorizedClient);
    }

    /**
     * 成功响应
     *
     * @param response
     * @param authorizedClient
     * @throws IOException
     */
    private void sendSuccessResponse(HttpServletResponse response, OAuth2AuthorizedClient authorizedClient) throws IOException {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        Map<String, Object> result = new HashMap<>();
        result.put("access_token", accessToken.getTokenValue());
        result.put("token_type", accessToken.getTokenType().getValue());
        result.put("expires_in", accessToken.getExpiresAt().toEpochMilli() - accessToken.getIssuedAt().toEpochMilli());
        result.put("refresh_token", refreshToken.getTokenValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 失败响应
     *
     * @param response
     * @throws IOException
     */
    private void sendFailureResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationFailureResponse errorResponse = AuthenticationFailureResponse.error(request.getRequestURI(), String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        byte[] errorResponseBytes = objectMapper.writeValueAsBytes(errorResponse);
        response.setContentLength(errorResponseBytes.length);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().print(new String(errorResponseBytes));
    }

    private static class PasswordHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final User user;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @param user
         * @throws IllegalArgumentException if the request is null
         */
        public PasswordHttpServletRequestWrapper(HttpServletRequest request, User user) {
            super(request);
            this.user = user;
        }

        @Override
        public String getParameter(String name) {
            if (OAuth2ParameterNames.USERNAME.equals(name)) {
                return user.getUsername();
            } else if (OAuth2ParameterNames.PASSWORD.equals(name)) {
                return user.getUsername();
            }
            return super.getParameter(name);
        }
    }
}
