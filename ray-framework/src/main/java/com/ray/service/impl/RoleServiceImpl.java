package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.RoleDto;
import com.ray.domain.dto.UpdateRoleDto;
import com.ray.domain.entity.Role;
import com.ray.domain.entity.RoleMenu;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.RoleVo;
import com.ray.mapper.RoleMapper;
import com.ray.service.RoleMenuService;
import com.ray.service.RoleService;
import com.ray.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-04-09 18:08:03
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKetByUserId(Long userId) {
        if(userId==1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        return  getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Role> roles = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        roleVos.stream().map(roleVo -> roleVo.setRemark(null)).collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(Long roleId, String status) {
        UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",status);
        updateWrapper.eq("Id",roleId);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult addRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateRoleInfo(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
//        建议先删除后添加而不是用更新，因为更新不能实现想要的效果
        LambdaQueryWrapper<RoleMenu> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(queryWrapper);
        List<RoleMenu> roleMenus = updateRoleDto.getMenuIds().stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

}


