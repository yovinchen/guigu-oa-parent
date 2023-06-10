package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-09
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单列表
     *
     * @return
     */
    List<SysMenu> findNodes();

    /**
     * 删除菜单
     *
     * @param id
     */
    void removeMenuById(Long id);
}
