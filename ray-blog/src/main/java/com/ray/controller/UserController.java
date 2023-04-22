package com.ray.controller;

import com.ray.annotation.SystemLog;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;
import com.ray.domain.vo.UserInfoVo;
import com.ray.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuris
 * @create 2023-04-02-17:06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
       return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody UserInfoVo userInfoVo){
        return userService.updateUserInfo(userInfoVo);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
