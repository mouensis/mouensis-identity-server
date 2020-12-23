package com.mouensis.server.identity.dto.menu;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 保存组织机构DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 23:54
 */
@Getter
@Setter
@ToString
@Schema(title = "更新组织机构DTO")
public class UpdateMenuDto implements Serializable {
    private static final long serialVersionUID = 8811413486022609847L;

    @Schema(title = "组织结构ID", example = "1")
    private Long id;

    @Schema(title = "菜单名称", example = "用户管理")
    private String menuName;

    @Schema(title = "菜单路径", example = "http://127.0.0.1:8080/admin/user")
    private String url;

    @Schema(title = "是否被禁用", example = "false")
    private Boolean disabled;

    @Schema(title = "菜单描述", example = "负责软件产品开发的部门")
    private String description;

    @Schema(title = "显示顺序", example = "10")
    private Long displayOrder;

}
