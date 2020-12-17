package com.mouensis.server.identity.security;

/**
 * 认证失败状态枚举
 *
 * @author zhuyuan
 * @date 2020/12/1 14:34
 */
enum AuthenticationFailureStatus {
    /**
     * 认证失败
     */
    FAILURE("900010"),

    /**
     * 用户名密码不正确
     */
    INVALID_USERNAME_OR_PASSWORD("900011"),

    /**
     * 账号过期
     */
    ACCOUNT_EXPIRED("900012"),

    /**
     * 密码过期
     */
    CREDENTIALS_EXPIRED("900013"),


    /**
     * 账号锁住
     */
    ACCOUNT_LOCKED("900014"),

    /**
     * 账号禁用
     */
    ACCOUNT_DISABLED("900015"),

    /**
     * 验证码不正确
     */
    INVALID_CAPTCHA("900018"),

    /**
     * TOKEN 错误
     */
    INVALID_BEARER_TOKEN("900021"),

    /**
     * TOKEN 过期
     */
    BEARER_TOKEN_EXPIRED("900022");

    private String status;

    AuthenticationFailureStatus(String status) {
        this.status = status;
    }

    public String status() {
        return this.status;
    }

    public AuthenticationFailureStatus statusOf(String status) {
        for (AuthenticationFailureStatus statusEnum : values()) {
            if (statusEnum.status().equals(status)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant AuthenticationFailureStatus." + status);
    }
}
