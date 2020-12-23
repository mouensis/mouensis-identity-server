package com.mouensis.server.identity.dto.menu;

import com.mouensis.framework.web.domain.QueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询菜单DTO
 *
 * @author zhuyuan
 * @date 2020/11/9 23:55
 */
@Getter
@Setter
@ToString
@Schema(title = "查询菜单DTO")
public class QueryMenuDto implements QueryDto, Serializable {
    private static final long serialVersionUID = -6006820687735126943L;

    @Schema(title = "菜单名称", example = "用户管理")
    private String menuName;

    @Schema(title = "菜单路径", example = "http://127.0.0.1:8080/admin/user")
    private String url;

    @Schema(title = "是否被禁用", example = "false")
    private Boolean disabled;
}
