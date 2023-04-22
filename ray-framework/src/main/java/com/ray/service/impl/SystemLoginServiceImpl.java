package com.ray.service.impl;

import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.domain.entity.User;
import com.ray.service.LoginService;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import com.ray.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuris
 * @create 2023-03-25-19:23
 */
@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 经过测试这个if貌似没用，因为前面有认证异常处理器，根本轮不到它
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        redisCache.setCacheObject("login:"+userId,loginUser);

        Map<String,String> map= new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);

    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

}
