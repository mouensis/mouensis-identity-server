package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.entity.UserEntity;
import com.mouensis.server.identity.repository.UserRepository;
import com.mouensis.server.identity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * 用户服务接口实现
 *
 * @author zhuyuan
 * @date 2020/10/21 18:34
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JpaRepository getJpaRepository() {
        return userRepository;
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updatePassword(Long id, String password) {
        validateHistoryPassword(id, password);
        UserEntity user = get(id);
        user.setPassword(password);
        update(user);
    }

    /**
     * 校验历史密码是否使用过
     *
     * @param id
     * @param password
     */
    private void validateHistoryPassword(Long id, String password) {
    }
}
