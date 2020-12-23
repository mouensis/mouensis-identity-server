package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 角色Repository
 *
 * @author zhuyuan
 * @date 2020/10/26 20:57
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
