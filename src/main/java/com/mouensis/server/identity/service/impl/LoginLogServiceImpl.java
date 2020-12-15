package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.repository.LoginLogRepository;
import com.mouensis.server.identity.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录日志服务接口实现
 *
 * @author zhuyuan
 * @date 2020/12/9 15:02
 */
@Service
@Slf4j
public class LoginLogServiceImpl implements LoginLogService {

    private LoginLogRepository loginLogRepository;

    @Autowired
    public void setLoginLogRepository(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public LoginLogRepository getJpaRepository() {
        return this.loginLogRepository;
    }
}
