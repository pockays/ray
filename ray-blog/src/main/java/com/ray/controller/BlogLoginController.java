package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.User;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuris
 * @create 2023-03-25-19:13
 */
@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
           throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);}


    @PostMapping("/logout")
    public ResponseResult logout(){

        return blogLoginService.logout();
    }

}

