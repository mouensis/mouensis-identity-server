package com.mouensis.server.identity.dto.permission;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 权限DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 23:24
 */
@Getter
@Setter
@ToString
@Schema(title = "权限DTO")
public class PermissionDto implements Serializable {
    private static final long serialVersionUID = 955962969079891116L;

    @Schema(title = "权限ID", example = "1")
    private Long id;

    @Schema(title = "权限名称", example = "用户管理")
    private String permissionName;

    @Schema(title = "权限描述", example = "针对用户的所有操作")
    private String description;

    @Schema(title = "父权限ID", example = "1")
    private Long parentId;

    @Schema(title = "显示顺序", example = "10")
    private Long displayOrder;
}
