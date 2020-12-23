package com.mouensis.server.identity.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户DTO
 *
 * @author zhuyuan
 * @date 2020/10/21 21:36
 */
@Getter
@Setter
@ToString
@Schema(title = "用户账号信息DTO")
public class UserDto implements Serializable {
    private static final long serialVersionUID = -8196136035053990133L;

    @Schema(title = "用户ID", example = "1")
    private Long id;

    @Schema(title = "用户账号", example = "admin")
    private String username;

    @Schema(title = "昵称", example = "管理员")
    private String nickname;

    @Schema(title = "联系电话", example = "18612345678")
    private String phone;

    @Schema(title = "邮箱", example = "zhuyuan2020@aliyun.com")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "账号过期时间", example = "2020-11-02 22:51:32")
    private Date accountExpiredTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "密码过期时间", example = "2020-11-02 22:51:35")
    private Date passwordExpiredTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "账号解锁时间", example = "2020-11-02 22:51:35")
    private Date accountUnlockedTime;
}
