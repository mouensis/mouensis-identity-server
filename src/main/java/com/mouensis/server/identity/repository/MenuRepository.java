package com.mouensis.server.identity.repository;

import com.mouensis.server.identity.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 菜单Repository
 *
 * @author zhuyuan
 * @date 2020/11/25 23:17
 */
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
