package com.mouensis.server.identity.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 更新用户电话DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 21:31
 */
@Getter
@Setter
@ToString
@Schema(title = "更新用户电话DTO")
public class UpdateUserPasswordDto implements Serializable {
    private static final long serialVersionUID = 8176487282415514537L;

    @Schema(title = "用户ID", example = "1")
    private Long id;

    @Schema(title = "账号密码", example = "123456")
    private String password;
}
