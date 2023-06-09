package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 更新用户状态
     *
     * @param id
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(Long id, Integer status) {
        //根据userid查询用户对象、
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态值
        if (status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        baseMapper.updateById(sysUser);
    }
}
