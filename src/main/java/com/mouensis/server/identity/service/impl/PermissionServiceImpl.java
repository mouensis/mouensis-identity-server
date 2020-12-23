package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.repository.PermissionRepository;
import com.mouensis.server.identity.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限服务实现类
 *
 * @author zhuyuan
 * @date 2020/11/25 23:14
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionRepository getJpaRepository() {
        return this.permissionRepository;
    }
}
