package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.repository.OrganizationRepository;
import com.mouensis.server.identity.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织结构服务接口实现类
 *
 * @author zhuyuan
 * @date 2020/11/2 16:13
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public OrganizationRepository getJpaRepository() {
        return organizationRepository;
    }
}
