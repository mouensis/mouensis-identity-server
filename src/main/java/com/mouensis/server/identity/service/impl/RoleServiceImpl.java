package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.repository.RoleRepository;
import com.mouensis.server.identity.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现类
 *
 * @author zhuyuan
 * @date 2020/11/25 23:14
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleRepository getJpaRepository() {
        return this.roleRepository;
    }
}
