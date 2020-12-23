package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.AuthorizedClientEntity;
import com.mouensis.server.identity.entity.pk.AuthorizedClientEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 已授权客户端
 *
 * @author zhuyuan
 * @date 2020/12/21 18:01
 */
public interface AuthorizedClientJpaRepository extends JpaRepository<AuthorizedClientEntity, AuthorizedClientEntityPk> {
}
