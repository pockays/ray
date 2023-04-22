package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.RoleDto;
import com.ray.domain.dto.UpdateRoleDto;
import com.ray.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-04-09 18:08:02
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKetByUserId(Long id);

    ResponseResult getRole(Long id);

    ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(Long roleId, String status);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult updateRoleInfo(UpdateRoleDto updateRoleDto);

}
