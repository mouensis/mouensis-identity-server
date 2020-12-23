package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.server.identity.dto.menu.MenuDto;
import com.mouensis.server.identity.dto.menu.QueryMenuDto;
import com.mouensis.server.identity.dto.menu.SaveMenuDto;
import com.mouensis.server.identity.dto.menu.UpdateMenuDto;
import com.mouensis.server.identity.entity.MenuEntity;
import com.mouensis.server.identity.service.MenuService;
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
 * 菜单Controller
 *
 * @author zhuyuan
 * @date 2020/10/22 20:20
 */
@RestController
@RequestMapping("/menus")
@Tag(name = "menu:*", description = "菜单管理")
@SecurityRequirement(name = "security-bearer")
public class MenuController {

    private MenuService menuService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "menu:create", summary = "创建菜单")
    public ResponseEntity<Void> create(@Parameter(description = "保存菜单信息请求", required = true)
                                       @RequestBody SaveMenuDto saveMenuDto) {
        MenuEntity saveMenuEntity = new MenuEntity();
        BeanUtils.copyProperties(saveMenuDto, saveMenuEntity);
        MenuEntity menuEntity = menuService.create(saveMenuEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "menu:delete", summary = "删除菜单")
    public ResponseEntity<Void> delete(@Parameter(description = "菜单ID", example = "1") @PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(operationId = "menu:update", summary = "全量更新菜单")
    public ResponseEntity<Void> update(@Parameter(description = "菜单ID", example = "1", required = true)
                                       @PathVariable Long id,
                                       @Parameter(description = "更新菜单信息请求", required = true)
                                       @RequestBody UpdateMenuDto updateMenuDto) {
        MenuEntity menuEntity = menuService.get(id);
        BeanUtils.copyProperties(updateMenuDto, menuEntity);
        menuService.update(menuEntity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "menu:get", summary = "根据ID查询菜单")
    public ResponseEntity<MenuDto> get(@Parameter(description = "菜单ID", example = "1") @PathVariable Long id) {
        MenuEntity menuEntity = menuService.get(id);
        MenuDto menuDto = new MenuDto();
        BeanUtils.copyProperties(menuEntity, menuDto);
        return ResponseEntity.ok(menuDto);
    }

    @GetMapping("")
    @Operation(operationId = "menu:list", summary = "分页查询菜单列表")
    public ResponseEntity<PageInfo<MenuDto>> list(@Parameter(description = "菜单ID") QueryMenuDto queryMenuDto,
                                                  @Parameter(description = "分页信息") Pagination pagination) {
        Example<MenuEntity> example = queryMenuDto.toExample(MenuEntity.class);
        Pageable pageable = pagination.toPageable(MenuEntity.class);
        Page<MenuEntity> entityPage = menuService.listPage(example, pageable);
        PageInfo<MenuDto> dtoPage = PageInfo.of(entityPage, menuEntity -> {
            MenuDto menuDto = new MenuDto();
            menuDto.setId(menuEntity.getId());
            menuDto.setMenuName(menuEntity.getMenuName());
            menuDto.setUrl(menuEntity.getUrl());
            menuDto.setDisabled(menuEntity.getDisabled());
            menuDto.setDescription(menuEntity.getDescription());
            menuDto.setDisplayOrder(menuEntity.getDisplayOrder());
            return menuDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
