package com.mouensis.server.identity.entity;

import com.mouensis.server.identity.entity.pk.AuthorizedClientEntityPk;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuyuan
 * @date 2020/12/21 17:53
 */
@Entity
@Table(name = "oauth2_authorized_client")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@IdClass(AuthorizedClientEntityPk.class)
public class AuthorizedClientEntity implements Serializable {
    private static final long serialVersionUID = -2767643447361174403L;
    /**
     * 客户端注册ID
     */
    @Id
    @Column(length = 36)
    private String authorizedClientId;

    /**
     * 账号名称
     */
    @Id
    @Column(length = 36)
    private String principalName;
    /**
     * 访问Token类型
     */
    private String accessTokenType;
    /**
     * 访问Token值
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String accessTokenValue;
    /**
     * 访问Token发布时间
     */
    private Date accessTokenIssuedAt;
    /**
     * 访问Token过期时间
     */
    private Date accessTokenExpiresAt;
    /**
     * 访问Token Scope
     */
    @Column(length = 1000)
    private String accessTokenScopes;
    /**
     * 刷新Token值
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String refreshTokenValue;
    /**
     * 刷新Token值事件
     */
    private Date refreshTokenIssuedAt;
    /**
     * 创建时间
     */
    private Date createdAt;

}
