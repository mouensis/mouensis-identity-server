package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.server.identity.dto.role.QueryRoleDto;
import com.mouensis.server.identity.dto.role.RoleDto;
import com.mouensis.server.identity.dto.role.SaveRoleDto;
import com.mouensis.server.identity.dto.role.UpdateRoleDto;
import com.mouensis.server.identity.entity.RoleEntity;
import com.mouensis.server.identity.service.RoleService;
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
 * 角色Controller
 *
 * @author zhuyuan
 * @date 2020/10/22 20:20
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "role:*", description = "角色管理")
@SecurityRequirement(name = "security-bearer")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "role:create", summary = "创建角色")
    public ResponseEntity<Void> create(@Parameter(description = "保存角色信息请求", required = true)
                                       @RequestBody SaveRoleDto saveRoleDto) {
        RoleEntity saveRoleEntity = new RoleEntity();
        BeanUtils.copyProperties(saveRoleDto, saveRoleEntity);
        RoleEntity roleEntity = roleService.create(saveRoleEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "role:delete", summary = "删除角色")
    public ResponseEntity<Void> delete(@Parameter(description = "角色ID", example = "1") @PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(operationId = "role:update", summary = "更新角色")
    public ResponseEntity<Void> update(@Parameter(description = "角色ID", example = "1", required = true)
                                       @PathVariable Long id,
                                       @Parameter(description = "更新角色信息请求", required = true)
                                       @RequestBody UpdateRoleDto updateRoleDto) {
        RoleEntity roleEntity = roleService.get(id);
        BeanUtils.copyProperties(updateRoleDto, roleEntity);
        roleService.update(roleEntity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "role:get", summary = "查询角色")
    public ResponseEntity<RoleDto> get(@Parameter(description = "角色ID", example = "1") @PathVariable Long id) {
        RoleEntity roleEntity = roleService.get(id);
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(roleEntity, roleDto);
        return ResponseEntity.ok(roleDto);
    }

    @GetMapping("")
    @Operation(operationId = "role:list", summary = "分页查询角色列表")
    public ResponseEntity<PageInfo<RoleDto>> list(@Parameter(description = "角色ID") QueryRoleDto queryRoleDto,
                                                  @Parameter(description = "分页信息") Pagination pagination) {
        Example<RoleEntity> example = queryRoleDto.toExample(RoleEntity.class);
        Pageable pageable = pagination.toPageable(RoleEntity.class);
        Page<RoleEntity> entityPage = roleService.listPage(example, pageable);
        PageInfo<RoleDto> dtoPage = PageInfo.of(entityPage, roleEntity -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(roleEntity.getId());
            roleDto.setRoleName(roleEntity.getRoleName());
            roleDto.setDescription(roleEntity.getDescription());
            roleDto.setDisplayOrder(roleEntity.getDisplayOrder());
            return roleDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
