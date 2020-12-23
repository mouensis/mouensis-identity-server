package com.mouensis.server.identity.service;

import com.mouensis.framework.core.service.GenericService;
import com.mouensis.server.identity.entity.UserEntity;

/**
 * 用户服务接口
 *
 * @author zhuyuan
 * @date 2020/10/21 18:34
 */
public interface UserService extends GenericService<UserEntity, Long> {
    /**
     * 根据用户名称返回用户信息
     *
     * @param username
     * @return
     */
    UserEntity getByUsername(String username);

    /**
     * 更新密码
     *
     * @param id
     * @param password
     */
    void updatePassword(Long id, String password);
}
