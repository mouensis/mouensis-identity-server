package com.mouensis.server.identity.dto.permission;

import com.mouensis.framework.web.domain.QueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询权限DTO
 *
 * @author zhuyuan
 * @date 2020/11/9 23:55
 */
@Getter
@Setter
@ToString
@Schema(title = "查询权限DTO")
public class QueryPermissionDto implements QueryDto, Serializable {
    private static final long serialVersionUID = -6006820687735126943L;

    @Schema(title = "权限名称", example = "用户管理")
    private String permissionName;
}
