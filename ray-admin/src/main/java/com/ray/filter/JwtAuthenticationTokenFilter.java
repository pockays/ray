package com.ray.filter;

import com.alibaba.fastjson.JSON;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.LoginUser;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.utils.JwtUtil;
import com.ray.utils.RedisCache;
import com.ray.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author liuris
 * @create 2023-03-27-8:52
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter  {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//       获取请求头中的token
        String token = request.getHeader("token");
//        如果没有token就不用认证了
        if (!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        try {
//            解析token
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
//            如果解析错误则需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
//        获取userid
        String userId = claims.getSubject();
//        用userid从redis里获取登录的用户
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
//        如果不存在该用户则需重新登陆
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
//        将该用户封装进SecurityContextHolder中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
