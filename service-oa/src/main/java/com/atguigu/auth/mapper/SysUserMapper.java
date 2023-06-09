package com.atguigu.auth.mapper;

import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-09
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
