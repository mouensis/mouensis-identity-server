package com.mouensis.server.identity.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 接口通用响应
 *
 * @author zhuyuan
 * @date 2020/12/1 14:22
 */
@Getter
@Setter
@ToString
public class AuthenticationFailureResponse {
    /**
     * 响应时间
     */
    private Date timestamp = new Date();
    /**
     * 请求路径
     */
    private String path;
    /**
     * 响应编码
     */
    private String code;
    /**
     * 响应描述
     */
    private String message;

    public AuthenticationFailureResponse() {

    }

    public AuthenticationFailureResponse(String path) {
        this.path = path;
    }

    protected AuthenticationFailureResponse(String path, String code, String message) {
        this.path = path;
        this.code = code;
        this.message = message;
    }

    public static AuthenticationFailureResponse error(String path, String code, String message) {
        return new AuthenticationFailureResponse(path, code, message);
    }

    public static AuthenticationFailureResponse error(String path, String code) {
        return error(path, code, null);
    }
}
