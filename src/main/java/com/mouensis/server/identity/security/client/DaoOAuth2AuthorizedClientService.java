package com.mouensis.server.identity.security.client;

import com.mouensis.server.identity.entity.AuthorizedClientEntity;
import com.mouensis.server.identity.entity.pk.AuthorizedClientEntityPk;
import com.mouensis.server.identity.repository.AuthorizedClientJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;

/**
 * 数据库实现
 *
 * @author zhuyuan
 * @date 2020/12/21 17:37
 */
public class DaoOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private AuthorizedClientJpaRepository authorizedClientJpaRepository;

    private ClientRegistrationRepository clientRegistrationRepository;

    private Function<OAuth2AuthorizedClientHolder, AuthorizedClientEntity> authorizedClientEntityMapper;

    public DaoOAuth2AuthorizedClientService() {
        this.authorizedClientEntityMapper = new OAuth2AuthorizedClientEntityMapper();
    }

    @Autowired
    public void setAuthorizedClientRepository(AuthorizedClientJpaRepository authorizedClientJpaRepository) {
        this.authorizedClientJpaRepository = authorizedClientJpaRepository;
    }

    @Autowired
    public void setClientRegistrationRepository(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
        AuthorizedClientEntity authorizedClient
                = this.authorizedClientJpaRepository.findById(
                new AuthorizedClientEntityPk(clientRegistrationId, principalName)).orElse(null);
        if (authorizedClient == null) {
            return null;
        }

        ClientRegistration clientRegistration = this.clientRegistrationRepository
                .findByRegistrationId(clientRegistrationId);
        if (clientRegistration == null) {
            throw new DataRetrievalFailureException(
                    "The ClientRegistration with id '" + clientRegistrationId + "' exists in the data source, "
                            + "however, it was not found in the ClientRegistrationRepository.");
        }
        OAuth2AccessToken.TokenType tokenType = null;
        if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(authorizedClient.getAccessTokenType())) {
            tokenType = OAuth2AccessToken.TokenType.BEARER;
        }
        String tokenValue = authorizedClient.getAccessTokenValue();
        Instant issuedAt = authorizedClient.getAccessTokenIssuedAt().toInstant();
        Instant expiresAt = authorizedClient.getAccessTokenExpiresAt().toInstant();
        Set<String> scopes = Collections.emptySet();
        String accessTokenScopes = authorizedClient.getAccessTokenScopes();
        if (accessTokenScopes != null) {
            scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt, scopes);
        OAuth2RefreshToken refreshToken = null;
        String refreshTokenValue = authorizedClient.getRefreshTokenValue();
        if (StringUtils.hasText(refreshTokenValue)) {
            Date refreshTokenIssuedAt = authorizedClient.getRefreshTokenIssuedAt();
            if (refreshTokenIssuedAt != null) {
                issuedAt = refreshTokenIssuedAt.toInstant();
            }
            refreshToken = new OAuth2RefreshToken(refreshTokenValue, issuedAt);
        }
        return new OAuth2AuthorizedClient(clientRegistration, principalName, accessToken, refreshToken);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(principal, "principal cannot be null");
        boolean existsAuthorizedClient = null != this.loadAuthorizedClient(
                authorizedClient.getClientRegistration().getRegistrationId(), principal.getName());
        if (existsAuthorizedClient) {
            updateAuthorizedClient(authorizedClient, principal);
        } else {
            try {
                insertAuthorizedClient(authorizedClient, principal);
            } catch (DuplicateKeyException ex) {
                updateAuthorizedClient(authorizedClient, principal);
            }
        }
    }

    /**
     * The default {@code Function} that maps {@link OAuth2AuthorizedClientHolder} to a
     * {@code List} of {@link AuthorizedClientEntity}.
     */
    public static class OAuth2AuthorizedClientEntityMapper
            implements Function<OAuth2AuthorizedClientHolder, AuthorizedClientEntity> {

        @Override
        public AuthorizedClientEntity apply(OAuth2AuthorizedClientHolder authorizedClientHolder) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientHolder.getAuthorizedClient();
            Authentication principal = authorizedClientHolder.getPrincipal();
            ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
            AuthorizedClientEntity authorizedClientEntity = new AuthorizedClientEntity();
            authorizedClientEntity.setAuthorizedClientId(clientRegistration.getRegistrationId());
            authorizedClientEntity.setPrincipalName(principal.getName());
            authorizedClientEntity.setAccessTokenType(accessToken.getTokenType().getValue());
            authorizedClientEntity.setAccessTokenValue(accessToken.getTokenValue());
            authorizedClientEntity.setAccessTokenIssuedAt(
                    Timestamp.from(accessToken.getIssuedAt()));
            authorizedClientEntity.setAccessTokenExpiresAt(
                    Timestamp.from(accessToken.getExpiresAt()));
            String accessTokenScopes = null;
            if (!CollectionUtils.isEmpty(accessToken.getScopes())) {
                accessTokenScopes = StringUtils.collectionToDelimitedString(accessToken.getScopes(), ",");
            }
            authorizedClientEntity.setAccessTokenScopes(accessTokenScopes);
            String refreshTokenValue = null;
            Date refreshTokenIssuedAt = null;
            if (refreshToken != null) {
                refreshTokenValue = refreshToken.getTokenValue();
                if (refreshToken.getIssuedAt() != null) {
                    refreshTokenIssuedAt = Timestamp.from(refreshToken.getIssuedAt());
                }
            }
            authorizedClientEntity.setRefreshTokenValue(refreshTokenValue);
            authorizedClientEntity.setRefreshTokenIssuedAt(refreshTokenIssuedAt);
            return authorizedClientEntity;
        }

    }

    private void insertAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        AuthorizedClientEntity clientEntity = this.authorizedClientEntityMapper
                .apply(new OAuth2AuthorizedClientHolder(authorizedClient, principal));
        this.authorizedClientJpaRepository.save(clientEntity);
    }

    private void updateAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        AuthorizedClientEntity clientEntity = this.authorizedClientEntityMapper
                .apply(new OAuth2AuthorizedClientHolder(authorizedClient, principal));
        this.authorizedClientJpaRepository.save(clientEntity);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        this.authorizedClientJpaRepository.deleteById(new AuthorizedClientEntityPk(clientRegistrationId, principalName));
    }

    /**
     * A holder for an {@link OAuth2AuthorizedClient} and End-User {@link Authentication}
     * (Resource Owner).
     */
    public static final class OAuth2AuthorizedClientHolder {

        private final OAuth2AuthorizedClient authorizedClient;

        private final Authentication principal;

        /**
         * Constructs an {@code OAuth2AuthorizedClientHolder} using the provided
         * parameters.
         *
         * @param authorizedClient the authorized client
         * @param principal        the End-User {@link Authentication} (Resource Owner)
         */
        public OAuth2AuthorizedClientHolder(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
            Assert.notNull(authorizedClient, "authorizedClient cannot be null");
            Assert.notNull(principal, "principal cannot be null");
            this.authorizedClient = authorizedClient;
            this.principal = principal;
        }

        /**
         * Returns the {@link OAuth2AuthorizedClient}.
         *
         * @return the {@link OAuth2AuthorizedClient}
         */
        public OAuth2AuthorizedClient getAuthorizedClient() {
            return this.authorizedClient;
        }

        /**
         * Returns the End-User {@link Authentication} (Resource Owner).
         *
         * @return the End-User {@link Authentication} (Resource Owner)
         */
        public Authentication getPrincipal() {
            return this.principal;
        }

    }
}
