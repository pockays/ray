package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.AddUserDto;
import com.ray.domain.dto.UpdateUserDto;
import com.ray.domain.entity.User;
import com.ray.domain.vo.UserInfoVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 20:49:35
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(UserInfoVo userInfoVo);

    ResponseResult register(User user);

    ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult adminUserInfo(Long id);

    ResponseResult updateAdminUserInfo(UpdateUserDto updateUserDto);
}
