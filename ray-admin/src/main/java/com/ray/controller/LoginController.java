package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.Menu;
import com.ray.domain.entity.User;
import com.ray.domain.vo.AdminUserInfoVo;
import com.ray.domain.vo.RoutersVo;
import com.ray.domain.vo.UserInfoVo;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.LoginService;
import com.ray.service.MenuService;
import com.ray.service.RoleService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.RedisCache;
import com.ray.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuris
 * @create 2023-03-25-19:13
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisCache redisCache;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
           throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);}

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
//        查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
//        查询角色信息
        List<String> roleKeyList = roleService.selectRoleKetByUserId(loginUser.getUser().getId());

        User user = loginUser.getUser();

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus =menuService.selectRouterMenuTreeByUserId(userId);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}

