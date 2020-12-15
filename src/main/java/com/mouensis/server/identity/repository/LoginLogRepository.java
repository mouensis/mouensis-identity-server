package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 登录日志Repository
 *
 * @author zhuyuan
 * @date 2020-12-13 23:02:28
 */
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, String> {
}
