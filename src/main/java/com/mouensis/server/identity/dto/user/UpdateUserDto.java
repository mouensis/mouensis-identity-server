package com.mouensis.server.identity.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新用户DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 21:31
 */
@Getter
@Setter
@ToString
@Schema(title = "更新用户DTO")
public class UpdateUserDto implements Serializable {
    private static final long serialVersionUID = 8176487282415514537L;

    @Schema(title = "用户ID", example = "1")
    private Long id;

    @Schema(title = "昵称", example = "管理员")
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "账号过期时间", example = "2020-11-02 15:51:32")
    private Date accountExpiredTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "密码过期时间", example = "2020-11-02 15:51:35")
    private Date passwordExpiredTime;

    @Schema(title = "归属组织结构ID", example = "1")
    private Long organizationId;
}
