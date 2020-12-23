package com.mouensis.server.identity.dto.role;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 23:24
 */
@Getter
@Setter
@ToString
@Schema(title = "角色DTO")
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 955962969079891116L;

    @Schema(title = "角色ID", example = "1")
    private Long id;

    @Schema(title = "角色名称", example = "管理员")
    private String roleName;

    @Schema(title = "角色描述", example = "负责分配账号")
    private String description;

    @Schema(title = "父角色ID", example = "1")
    private Long parentId;

    @Schema(title = "显示顺序", example = "10")
    private Long displayOrder;
}
