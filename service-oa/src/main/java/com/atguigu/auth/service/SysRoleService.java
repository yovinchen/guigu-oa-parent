package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * ClassName: SysRoleService
 * Package: com.atguigu.auth.service
 *
 * @author yovinchen
 * @Create 2023/6/1 23:03
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 查询所有角色和当前用户所属角色
     *
     * @param userId
     * @return
     */
    Map<String, Object> findRoleByUserId(Long userId);

    /**
     * 为用户分配角色
     *
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
