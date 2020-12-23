package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.server.identity.dto.permission.PermissionDto;
import com.mouensis.server.identity.dto.permission.QueryPermissionDto;
import com.mouensis.server.identity.dto.permission.SavePermissionDto;
import com.mouensis.server.identity.dto.permission.UpdatePermissionDto;
import com.mouensis.server.identity.entity.PermissionEntity;
import com.mouensis.server.identity.service.PermissionService;
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
 * 权限Controller
 *
 * @author zhuyuan
 * @date 2020/10/22 20:20
 */
@RestController
@RequestMapping("/permissions")
@Tag(name = "permission:*", description = "权限管理")
@SecurityRequirement(name = "security-bearer")
public class PermissionController {

    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "permission:create", summary = "创建权限")
    public ResponseEntity<Void> create(@Parameter(description = "保存权限信息请求", required = true)
                                       @RequestBody SavePermissionDto savePermissionDto) {
        PermissionEntity savePermissionEntity = new PermissionEntity();
        BeanUtils.copyProperties(savePermissionDto, savePermissionEntity);
        PermissionEntity permissionEntity = permissionService.create(savePermissionEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "permission:delete", summary = "删除权限")
    public ResponseEntity<Void> delete(@Parameter(description = "权限ID", example = "1") @PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(operationId = "permission:update", summary = "全量更新权限")
    public ResponseEntity<Void> update(@Parameter(description = "权限ID", example = "1", required = true)
                                       @PathVariable Long id,
                                       @Parameter(description = "更新权限信息请求", required = true)
                                       @RequestBody UpdatePermissionDto updatePermissionDto) {
        PermissionEntity permissionEntity = permissionService.get(id);
        BeanUtils.copyProperties(updatePermissionDto, permissionEntity);
        permissionService.update(permissionEntity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "permission:get", summary = "根据ID查询权限")
    public ResponseEntity<PermissionDto> get(@Parameter(description = "权限ID", example = "1") @PathVariable Long id) {
        PermissionEntity permissionEntity = permissionService.get(id);
        PermissionDto permissionDto = new PermissionDto();
        BeanUtils.copyProperties(permissionEntity, permissionDto);
        return ResponseEntity.ok(permissionDto);
    }

    @GetMapping("")
    @Operation(operationId = "permission:list", summary = "分页查询权限列表")
    public ResponseEntity<PageInfo<PermissionDto>> list(@Parameter(description = "权限ID") QueryPermissionDto queryPermissionDto,
                                                        @Parameter(description = "分页信息") Pagination pagination) {
        Example<PermissionEntity> example = queryPermissionDto.toExample(PermissionEntity.class);
        Pageable pageable = pagination.toPageable(PermissionEntity.class);
        Page<PermissionEntity> entityPage = permissionService.listPage(example, pageable);
        PageInfo<PermissionDto> dtoPage = PageInfo.of(entityPage, permissionEntity -> {
            PermissionDto permissionDto = new PermissionDto();
            permissionDto.setId(permissionEntity.getId());
            permissionDto.setPermissionName(permissionEntity.getPermissionName());
            permissionDto.setDescription(permissionEntity.getDescription());
            permissionDto.setDisplayOrder(permissionEntity.getDisplayOrder());
            return permissionDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
