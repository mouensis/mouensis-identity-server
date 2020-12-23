package com.mouensis.server.identity.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 认证成功响应
 *
 * @author zhuyuan
 * @date 2020/12/17 20:09
 */
@Getter
@Setter
@ToString
public class AuthenticationSuccessResponse implements Serializable {
    private static final long serialVersionUID = -699220712411904059L;
    /**
     * 响应时间
     */
    private Date timestamp = new Date();

    /**
     * token值
     */
    private String token;

    public AuthenticationSuccessResponse(String token) {
        this.token = token;
    }
}
