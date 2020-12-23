package com.mouensis.server.identity.security;

import com.mouensis.server.identity.entity.UserEntity;
import com.mouensis.server.identity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Date;

/**
 * API用户详情服务接口
 *
 * @author zhuyuan
 * @date 2020/12/1 10:49
 */
@Slf4j
class ApiUserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        String password = userEntity.getPassword();
        boolean enabled = !userEntity.isDisabled();
        boolean accountNonExpired = userEntity.getAccountExpiredTime() == null
                || new Date().before(userEntity.getAccountExpiredTime());
        boolean credentialsNonExpired = userEntity.getPasswordExpiredTime() == null
                || new Date().before(userEntity.getPasswordExpiredTime());
        boolean accountNonLocked = userEntity.getAccountUnlockedTime() == null
                || new Date().after(userEntity.getAccountUnlockedTime());
        return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, Collections.emptyList());
    }
}
