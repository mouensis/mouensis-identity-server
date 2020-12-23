package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户Repository
 *
 * @author zhuyuan
 * @date 2020/10/21 18:17
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {
    /**
     * 根据用户名称查询用户信息
     *
     * @param username
     * @return
     */
    UserEntity findByUsername(String username);
}
