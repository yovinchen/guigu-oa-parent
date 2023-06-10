package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

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

    /**
     * 根据角色获取菜单
     *
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuByRoleId(Long roleId);

    /**
     * 给角色分配权限
     *
     * @param assignMenuVo
     */
    void doAssign(AssginMenuVo assignMenuVo);
}
