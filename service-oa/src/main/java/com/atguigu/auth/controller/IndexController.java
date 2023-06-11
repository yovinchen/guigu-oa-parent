package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.execption.GuiguException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName: IndexController
 * Package: com.atguigu.auth.controller
 * 后台登录登出
 *
 * @author yovinchen
 * @Create 2023/6/8 20:32
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {


    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     *
     * @return
     */
    @ApiOperation("登陆")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin");
//        return Result.ok(map);

        //获取输入用户名和密码,根据用户名查询数据库
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginVo.getUsername()));
        //用户信息是否存在
        if (sysUser == null) {
            throw new GuiguException(201, "用户不存在");
        }
        //判断密码
        if (!Objects.equals(sysUser.getPassword(), MD5.encrypt(loginVo.getPassword()))) {
            throw new GuiguException(201, "密码错误");
        }
        //判断用户是否被禁用
        if (sysUser.getStatus() == 0) {
            throw new GuiguException(201, "用户已经被禁用，请联系管理员");
        }
        //利用 JWT 根据用户的id和用户名生成Token
        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        //返回
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {

        //获取请求头中Token字符串，解密出用户信息
        String token = request.getHeader("token");

        //根据用户信息查询数据库，获取用户信息
        Long userId =JwtHelper.getUserId(token);
        SysUser sysUser = sysUserService.getById(userId);

        //根据用户id获取用户可以操作菜单列表
        //查询数据库，动态构建路由结构
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //根据用户id获取用户可以操作按钮列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        //返回相应数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        // 返回用户可以操作的菜单
        map.put("routers", routerList);

        // 返回用户可以操作的按钮
        map.put("buttons", permsList);
        return Result.ok(map);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

}
