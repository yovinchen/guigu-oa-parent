package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SysRoleService
 * Package: com.atguigu.auth.controller
 *
 * @author yovinchen
 * @Create 2023/6/6 17:19
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询所有角色和当前用户所属角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = sysRoleService.findRoleByUserId(userId);
        return Result.ok(roleMap);
    }

    /**
     * 为用户分配角色
     *
     * @param assginRoleVo
     * @return
     */
    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @ApiOperation("查询所有角色")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(sysRoleService.list());
    }

    /**
     * 条件分页查询
     * page 当前页
     * limit 每页显示记录数
     *
     * @return
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //调用service的方法实现
        //1 创建Page对象，传递分页相关参数
        //page 当前页  limit 每页显示记录数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            //封装 like模糊查询
            wrapper.like(SysRole::getRoleName, roleName);
        }

        //3 调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole role) {
        //调用service的方法
        boolean is_success = sysRoleService.save(role);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 修改角色-根据id查询
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    /**
     * 修改角色-最终修改
     *
     * @param role
     * @return
     */
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role) {
        //调用service的方法
        boolean is_success = sysRoleService.updateById(role);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysRoleService.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 批量删除
     * 前端数组 [1,2,3]
     *
     * @param idList
     * @return
     */
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
}