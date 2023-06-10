package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleMenuService;
import com.atguigu.auth.utils.MenuHelper;
import com.atguigu.common.execption.GuiguException;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.MetaVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 菜单列表
     *
     * @return
     */
    @Override
    public List<SysMenu> findNodes() {
        // 1、查询所有 的数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);

        // 2、构建树形结构

        return MenuHelper.buildTree(sysMenuList);
    }

    /**
     * 删除菜单
     *
     * @param id
     */
    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有下一层菜单
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId, id);

        Integer count = baseMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) {
            throw new GuiguException(201, "菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        //1 查询所有菜单- 添加条件 status=1

//        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
//        wrapperSysMenu.eq(SysMenu::getStatus, 1);
//        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapperSysMenu);

        List<SysMenu> allSysMenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        //2 根据角色id roleId查询 角色菜单关系表里面 角色id对应所有的菜单id
//        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
//        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
//        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        //3 根据获取菜单id，获取对应菜单对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        //3.1 拿着菜单id 和所有菜单集合里面id进行比较，如果相同封装
        allSysMenuList.forEach(item -> {
            if (menuIdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });

        //4 返回规定树形显示格式菜单列表
        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);
        return sysMenuList;
    }

    /**
     * 给角色分配权限
     *
     * @param assignMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assignMenuVo) {
        //根据角色id 删除菜单角色表 分配数据
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId()));
        //从参数里面获取角色新分配菜单id列表，
        //进行遍历，把每个id 数据添加菜单角色表
        for (Long menuId : assignMenuVo.getMenuIdList()) {
            if (StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu rolePermission = new SysRoleMenu();
            rolePermission.setRoleId(assignMenuVo.getRoleId());
            rolePermission.setMenuId(menuId);
            sysRoleMenuService.save(rolePermission);
        }
    }

    /**
     * 获取用户操作菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenusList = null;

        // 1、判断当前用户是否是管理员       userId=1 是管理员
        // 1.1、 如果是管理员，查询所有菜单列表
        if (userId == 1) {
            // 查询所有菜单列表
            sysMenusList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {

            // 1.2、如果不是管理员，根据 userId 查询可以操作菜单列表
            // 多表关联查询:sys_role、sys_role_menu、sys_menu
            sysMenusList = baseMapper.findUserMenuListByUserId(userId);
        }
        // 2、把查询出来的数据列表， 构建成框架要求的路由结构
        // 先构建树形结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenusList);
        // 构建框架要求的路由结构
        return this.buildRouter(sysMenuTreeList);
    }

    /**
     * 构建框架要求的路由结构
     *
     * @param menus
     * @return
     */
    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        // 创建 list 集合，存值最终数据
        List<RouterVo> routers = new ArrayList<>();
        // menus 遍历
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            // 下一层数据
            List<SysMenu> children = menu.getChildren();
            if (menu.getType() == 1) {
                // 加载隐藏路由
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    // 递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;

    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    /**
     * 获取用户操作按钮
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        // 1、判断是否是管理员，如果是管理员，查询所有按钮列表
        List<SysMenu> sysMenusList = null;
        if (userId == 1) {
            // 查询所有菜单列表
            sysMenusList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            // 2、如果不是管理员，根据userId查询可以操作按钮列表
            // 多表关联查询:sys_role、sys_role_menu、sys_menu
            sysMenusList = baseMapper.findUserMenuListByUserId(userId);
        }

        // 3、从查询出来的数据里面，获取可以操作按钮值的List集合，返回
//        List<String> resultList = new ArrayList<>();
//        for (SysMenu item : sysMenusList) {
//            if (item.getType() == 2) {
//                resultList.add(item.getPerms());
//            }
//        }
//        return resultList;

        return sysMenusList.stream()
                .filter(item -> item.getType() == 2)
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
    }

}
