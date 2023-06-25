package com.atguigu.auth.service;

import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-09
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 更新用户状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据用户名进行查询
     *
     * @param username
     * @return
     */
    SysUser getUserByUsername(String username);

    /**
     * 获取当前用户
     *
     * @return
     */
    Map<String, Object> getCurrentUser();
}
