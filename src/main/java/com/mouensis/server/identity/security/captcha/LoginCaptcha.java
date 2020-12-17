package com.mouensis.server.identity.security.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 验证码
 *
 * @author zhuyuan
 * @date 2020/12/16 15:10
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginCaptcha implements Serializable {
    /**
     * 验证码数字
     */
    private String code;
    /**
     * 验证码图片
     */
    private byte[] image;
}
