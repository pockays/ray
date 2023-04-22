package com.ray.service;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;

/**
 * @author liuris
 * @create 2023-03-25-19:22
 */
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
