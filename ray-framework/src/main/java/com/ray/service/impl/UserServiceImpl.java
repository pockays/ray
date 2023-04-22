package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.AddUserDto;
import com.ray.domain.dto.UpdateUserDto;
import com.ray.domain.entity.Role;
import com.ray.domain.entity.User;
import com.ray.domain.entity.UserRole;
import com.ray.domain.vo.*;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.UserMapper;
import com.ray.service.RoleService;
import com.ray.service.UserRoleService;
import com.ray.service.UserService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 20:49:36
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoVo userInfoVo) {
        User user = BeanCopyUtils.copyBean(userInfoVo, User.class);
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
//        之后可以用verification重构
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_EXIST);
        }

        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        save(user);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName, userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<User> users = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(users, UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos,page.getTotal()));

    }

    @Transactional
    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(addUserDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if(phonenumberExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }


        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);
        List<UserRole> userRoles = addUserDto.getRoleIds().stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminUserInfo(Long id) {
        User user = getById(id);
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> list = userRoleService.list(queryWrapper);
        List<Long> roleIds = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.list();
        AdminUserVo vo = BeanCopyUtils.copyBean(user, AdminUserVo.class);
        return ResponseResult.okResult(new AdminUserRoleVo(roleIds,roles,vo));
    }

    @Override
    @Transactional
    public ResponseResult updateAdminUserInfo(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        if(!updateById(user)){
            throw new SystemException(AppHttpCodeEnum.USER_NO_EXIST);
        }
        List<UserRole> userRoles = updateUserDto.getRoleIds().stream().map(roleId -> new UserRole(updateUserDto.getId(), roleId)).collect(Collectors.toList());
        LambdaQueryWrapper<UserRole> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, updateUserDto.getId());
        userRoleService.remove(queryWrapper);
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean phonenumberExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(User::getPhonenumber,phonenumber);
        return count(queryWrapper)>0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
}


