package com.mouensis.server.identity.service.impl;

import com.mouensis.server.identity.repository.MenuRepository;
import com.mouensis.server.identity.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单服务实现类
 *
 * @author zhuyuan
 * @date 2020/11/25 23:14
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public MenuRepository getJpaRepository() {
        return this.menuRepository;
    }
}
