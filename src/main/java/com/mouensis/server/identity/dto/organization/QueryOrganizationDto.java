package com.mouensis.server.identity.dto.organization;

import com.mouensis.framework.web.domain.QueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询组织结构DTO
 *
 * @author zhuyuan
 * @date 2020/11/9 23:55
 */
@Getter
@Setter
@ToString
@Schema(title = "查询组织结构DTO")
public class QueryOrganizationDto implements QueryDto, Serializable {
    private static final long serialVersionUID = -6006820687735126943L;

    @Schema(title = "组织机构名称", example = "研发部")
    private String organizationName;
}
