package com.mouensis.server.identity.controller;

import com.mouensis.framework.web.domain.PageInfo;
import com.mouensis.framework.web.domain.Pagination;
import com.mouensis.framework.web.util.PaginationUtils;
import com.mouensis.server.identity.dto.log.LoginLogDto;
import com.mouensis.server.identity.dto.log.QueryLoginLogDto;
import com.mouensis.server.identity.entity.LoginLogEntity;
import com.mouensis.server.identity.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志Controller
 *
 * @author zhuyuan
 * @date 2020-12-13 23:01
 */
@RestController
@RequestMapping("/login-logs")
@Tag(name = "login-log:*", description = "菜单管理")
@SecurityRequirement(name = "security-bearer")
public class LoginLogController {

    private LoginLogService loginLogService;

    @Autowired
    public void setLoginLogService(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping("")
    @Operation(operationId = "login-log:list", summary = "分页查询菜单列表")
    public ResponseEntity<PageInfo<LoginLogDto>> list(@Parameter(description = "菜单ID") QueryLoginLogDto queryLoginLogDto,
                                                      @Parameter(description = "分页信息") Pagination pagination) {
        Example<LoginLogEntity> example = queryLoginLogDto.toExample(LoginLogEntity.class);
        Pageable pageable = PaginationUtils.toPageable(pagination, LoginLogEntity.class);
        Page<LoginLogEntity> entityPage = loginLogService.listPage(example, pageable);
        PageInfo<LoginLogDto> dtoPage = PageInfo.of(entityPage, loginLogEntity -> {
            LoginLogDto loginLogDto = new LoginLogDto();
            BeanUtils.copyProperties(loginLogEntity, loginLogDto);
            return loginLogDto;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
