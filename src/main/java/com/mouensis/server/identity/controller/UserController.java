package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.server.identity.dto.user.*;
import com.mouensis.server.identity.entity.UserEntity;
import com.mouensis.server.identity.service.UserService;
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
 * 用户Controller
 *
 * @author zhuyuan
 * @date 2020/10/22 20:20
 */
@RestController
@RequestMapping("/users")
@Tag(name = "user:*", description = "用户管理")
@SecurityRequirement(name = "security-bearer")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "user:create", summary = "创建用户")
    public ResponseEntity<Void> create(@Parameter(description = "保存用户账号信息请求", required = true)
                                       @RequestBody SaveUserDto saveUserDto) {
        UserEntity saveUserEntity = new UserEntity();
        BeanUtils.copyProperties(saveUserDto, saveUserEntity);
        UserEntity userEntity = userService.create(saveUserEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "user:delete", summary = "删除用户")
    public ResponseEntity<Void> delete(@Parameter(description = "用户账号ID", example = "1") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "user:update", summary = "更新用户")
    public ResponseEntity<Void> update(@Parameter(description = "用户账号ID", example = "1")
                                       @PathVariable Long id,
                                       @Parameter(description = "更新用户账号信息请求", required = true)
                                       @RequestBody UpdateUserDto updateUserDto) {
        UserEntity userEntity = userService.get(id);
        BeanUtils.copyProperties(updateUserDto, userEntity);
        userService.update(userEntity);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    @Operation(operationId = "user:update_password", summary = "更新用户密码")
    public ResponseEntity<Void> updatePassword(@Parameter(description = "用户账号ID", example = "1")
                                               @PathVariable Long id,
                                               @Parameter(description = "更新用户账号密码信息请求", required = true)
                                               @RequestBody UpdateUserPasswordDto passwordDto) {
        userService.updatePassword(id, passwordDto.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(operationId = "user:get", summary = "查询用户")
    public ResponseEntity<UserDto> get(@Parameter(description = "用户账号ID", example = "1") @PathVariable Long id) {
        UserEntity userEntity = userService.get(id);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("")
    @Operation(operationId = "user:list", summary = "分页查询用户列表")
    public ResponseEntity<PageInfo<UserDto>> list(@Parameter(description = "用户账号ID") QueryUserDto queryUserDto,
                                                  @Parameter(description = "分页信息") Pagination pagination) {
        Example<UserEntity> example = queryUserDto.toExample(UserEntity.class);
        Pageable pageable = pagination.toPageable(UserEntity.class);
        Page<UserEntity> entityPage = userService.listPage(example, pageable);
        PageInfo<UserDto> dtoPage = PageInfo.of(entityPage, userEntity -> {
            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setUsername(userEntity.getUsername());
            userDto.setNickname(userEntity.getNickname());
            userDto.setPhone(userEntity.getPhone());
            userDto.setEmail(userEntity.getEmail());
            userDto.setAccountExpiredTime(userEntity.getAccountExpiredTime());
            userDto.setPasswordExpiredTime(userEntity.getPasswordExpiredTime());
            return userDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
