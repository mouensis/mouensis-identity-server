package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 组织结构Repository
 *
 * @author zhuyuan
 * @date 2020/10/26 20:59
 */
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
}
