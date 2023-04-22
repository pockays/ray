package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Menu;
import com.ray.domain.entity.RoleMenu;
import com.ray.domain.vo.AdminMenuVo;
import com.ray.domain.vo.MenuInfoVo;
import com.ray.domain.vo.MenuVo;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.mapper.MenuMapper;
import com.ray.service.MenuService;
import com.ray.service.RoleMenuService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-09 18:01:02
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus=null;
        if(SecurityUtils.isAdmin()){
            menus = menuMapper.selectAllRouterMenu();
        }else{
            menus=menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        List<Menu> menuTree = buliderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult menuList(String status, String menuName) {

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.orderByAsc(Menu::getId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult queryMenu(Long id) {
        Menu menu = getById(id);
        MenuInfoVo menuInfoVo = BeanCopyUtils.copyBean(menu, MenuInfoVo.class);
        return ResponseResult.okResult(menuInfoVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        Menu menu = getById(id);
        List<Menu> menus = list();
        if(!CollectionUtils.isEmpty((getChildren(menu,menus)))){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "存在子菜单不允许删除");
        }
        removeById(id);
        return ResponseResult.okResult();
    }



    @Override
    public ResponseResult treeSelect() {
        List<Menu> menus = list();
        List<AdminMenuVo> adminMenuVos = BeanCopyUtils.copyBeanList(menus, AdminMenuVo.class);
        adminMenuVos.stream().map(adminMenuVo -> adminMenuVo.setLabel(getById(adminMenuVo.getId()).getMenuName())).collect(Collectors.toList());
        List<AdminMenuVo> menuTree = buliderAdminMenuVoTree(adminMenuVos,0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long roleId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus=null;
        if(roleId==1L){
            menus = menuMapper.selectAllRoleMenu();
        }else{
            menus=menuMapper.selectRoleMenuTreeByRoleId(roleId);
        }
        menus.stream().map(o -> o.setLabel(o.getMenuName())).collect(Collectors.toList());
        List<Menu> menuTrees = buliderMenuTree(menus,0L);

        List<AdminMenuVo> adminMenuVos = copyChildrenBeanList(menuTrees, AdminMenuVo.class);


        return ResponseResult.okResult(adminMenuVos);
    }

//这个方法是废案，如果想在形成树后再加label可以用这个（但是要改一下）
//    private String getLabel(Menu menu) {
//        String menuName = menu.getMenuName();
//        menu.setLabel(menuName);
//        menu.getChildren().stream().map(o -> o.setLabel(getLabel(o))).collect(Collectors.toList());
//        return  menuName;
//    }


    private  List<AdminMenuVo> copyChildrenBeanList(List<?> menuTrees, Class<AdminMenuVo> adminMenuVo) {
        List<AdminMenuVo> adminMenuVos = BeanCopyUtils.copyBeanList(menuTrees, adminMenuVo);
        adminMenuVos.stream().map(o -> o.setChildren(copyChildrenBeanList(o.getChildren(), adminMenuVo))).collect(Collectors.toList());
        return adminMenuVos;
    }


    private List<AdminMenuVo> buliderAdminMenuVoTree(List<AdminMenuVo> menus, Long parentId) {
        List<AdminMenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getAdminMenuVoChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<AdminMenuVo> getAdminMenuVoChildren(AdminMenuVo menu,List<AdminMenuVo> menus) {
        List<AdminMenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getAdminMenuVoChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<Menu> buliderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu,List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}


