package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限Repository
 *
 * @author zhuyuan
 * @date 2020/10/26 20:59
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
