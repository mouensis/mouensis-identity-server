package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.server.identity.dto.organization.OrganizationDto;
import com.mouensis.server.identity.dto.organization.QueryOrganizationDto;
import com.mouensis.server.identity.dto.organization.SaveOrganizationDto;
import com.mouensis.server.identity.dto.organization.UpdateOrganizationDto;
import com.mouensis.server.identity.entity.OrganizationEntity;
import com.mouensis.server.identity.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 组织机构Controller
 *
 * @author zhuyuan
 * @date 2020/10/22 20:20
 */
@RestController
@RequestMapping("/organizations")
@Tag(name = "organization:*", description = "组织机构管理")
@SecurityRequirement(name = "security-bearer")
public class OrganizationController {

    private OrganizationService organizationService;

    @Autowired
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "organization:create", summary = "创建组织机构")
    public ResponseEntity<Void> create(@Parameter(description = "保存组织机构请求", required = true)
                                       @RequestBody SaveOrganizationDto saveOrganizationDto) {
        OrganizationEntity saveOrganizationEntity = new OrganizationEntity();
        BeanUtils.copyProperties(saveOrganizationDto, saveOrganizationEntity);
        OrganizationEntity organizationEntity = organizationService.create(saveOrganizationEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "organization:delete", summary = "删除组织机构")
    public ResponseEntity<Void> delete(@Parameter(description = "组织机构ID", example = "1") @PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(operationId = "organization:update", summary = "更新组织机构")
    public ResponseEntity<Void> update(@Parameter(description = "组织机构ID", example = "1", required = true)
                                       @PathVariable Long id,
                                       @Parameter(description = "更新组织机构请求", required = true)
                                       @RequestBody UpdateOrganizationDto updateOrganizationDto) {
        OrganizationEntity organizationEntity = organizationService.get(id);
        BeanUtils.copyProperties(updateOrganizationDto, organizationEntity);
        organizationService.update(organizationEntity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "organization:get", summary = "查询组织机构")
    public ResponseEntity<OrganizationDto> get(@Parameter(description = "组织机构ID", example = "1") @PathVariable Long id) {
        OrganizationEntity organizationEntity = organizationService.get(id);
        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organizationEntity, organizationDto);
        return ResponseEntity.ok(organizationDto);
    }

    @GetMapping("")
    @Operation(operationId = "organization:list", summary = "分页查询菜单列表")
    public ResponseEntity<PageInfo<OrganizationDto>> list(@Parameter(description = "组织机构ID") QueryOrganizationDto queryOrganizationDto,
                                                          @Parameter(description = "分页") Pagination pagination) {
        Example<OrganizationEntity> example = queryOrganizationDto.toExample(OrganizationEntity.class);
        Pageable pageable = pagination.toPageable(OrganizationEntity.class);
        Page<OrganizationEntity> entityPage = organizationService.listPage(example, pageable);
        PageInfo<OrganizationDto> dtoPage = PageInfo.of(entityPage, organizationEntity -> {
            OrganizationDto organizationDto = new OrganizationDto();
            organizationDto.setId(organizationEntity.getId());
            organizationDto.setOrganizationName(organizationEntity.getOrganizationName());
            organizationDto.setDescription(organizationEntity.getDescription());
            organizationDto.setDisplayOrder(organizationEntity.getDisplayOrder());
            return organizationDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
