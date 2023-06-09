package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: TestMpDemo1
 * Package: com.atguigu.auth
 *
 * @author yovinchen
 * @Create 2023/6/1 23:04
 */
@SpringBootTest
public class TestMpDemo1 {

    /**
     * MyBatisPlus 对 service 层和 dao 层都做了很好的封装，直接调对应的CRUD方法就行
     */
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 使用MP 封装的 service 来操作数据库，查询所有记录
     */
    @Test
    public void getAllByService() {
        List<SysRole> list = sysRoleService.list();
        list.forEach(System.out::println);
    }

    /**
     * 使用MP 封装的 mapper查询所有记录
     */
    @Test
    public void getAllByMapper() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(System.out::println);
    }

    /**
     * 添加操作
     */
    @Test
    public void insert() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        int result = sysRoleMapper.insert(sysRole);

        System.out.println(result); //影响的行数
        System.out.println(sysRole.getId()); //id自动回填
    }

    /**
     * 修改操作
     */
    @Test
    public void updateById() {
        // 根据id查询
        SysRole sysRole = sysRoleMapper.selectById(9);
        // 设置修改值
        sysRole.setRoleName("角色管理员1");
        // 调用方法实现最终修改
        int update = sysRoleMapper.updateById(sysRole);
        // 受影响的行数
        System.out.println("update = " + update);
    }

    /**
     * 根据id删除
     */
    @Test
    public void deleteById() {
        int delete = sysRoleMapper.deleteById(9);
        // 受影响的行数
        System.out.println("delete = " + delete);
    }

    /**
     * 批量删除
     */
    @Test
    public void deleteBatchByIds() {
        // 或者下面这种写法
        int delete = sysRoleMapper.deleteBatchIds(Arrays.asList(8, 9));
        // 受影响的行数
        System.out.println("ids = " + delete);
    }

    /**
     * 条件查询
     */
    @Test
    public void testQueryWrapper1() {

        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", "系统管理员");

        List<SysRole> list = sysRoleMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    /**
     * 条件查询
     */
    @Test
    public void testQueryWrapper2() {

        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        //java 8 中写法
        queryWrapper.eq(SysRole::getRoleName, "系统管理员");

        List<SysRole> list = sysRoleMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

}
