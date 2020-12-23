package com.mouensis.server.identity.dto.role;

import com.mouensis.framework.web.domain.QueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询角色DTO
 *
 * @author zhuyuan
 * @date 2020/11/9 23:55
 */
@Getter
@Setter
@ToString
@Schema(title = "查询角色DTO")
public class QueryRoleDto implements QueryDto, Serializable {
    private static final long serialVersionUID = -6006820687735126943L;

    @Schema(title = "角色名称", example = "管理员")
    private String roleName;
}
