package com.mouensis.server.identity.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 保存用户DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 21:31
 */
@Getter
@Setter
@ToString
@Schema(title = "保存用户DTO")
public class SaveUserDto implements Serializable {
    private static final long serialVersionUID = 8176487282415514537L;

    @Schema(title = "用户账号", example = "admin")
    private String username;

    @Schema(title = "账号密码", example = "123456")
    private String password;

    @Schema(title = "联系电话", example = "18612345678")
    private String phone;

    @Schema(title = "邮箱", example = "zhuyuan2020@aliyun.com")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "账号过期时间", example = "2020-11-02 15:51:32")
    private Date accountExpiredTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "密码过期时间", example = "2020-11-02 15:51:35")
    private Date passwordExpiredTime;

    @Schema(title = "归属组织结构ID", example = "1")
    private Long organizationId;
}
