package com.mouensis.server.identity.dto.organization;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 组织结构DTO
 *
 * @author zhuyuan
 * @date 2020/11/2 23:24
 */
@Getter
@Setter
@ToString
@Schema(title = "组织机构DTO")
public class OrganizationDto implements Serializable {
    private static final long serialVersionUID = 955962969079891116L;

    @Schema(title = "组织结构ID", example = "1")
    private Long id;

    @Schema(title = "组织机构名称", example = "研发部")
    private String organizationName;

    @Schema(title = "组织机构描述", example = "负责软件产品开发的部门")
    private String description;

    @Schema(title = "父组织机构ID", example = "1")
    private Long parentId;

    @Schema(title = "显示顺序", example = "10")
    private Long displayOrder;
}
