package com.example.familyeducation.filter;

import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:自定义过滤器，获取请求头中的token并解析
 * @Author:小菜
 * @Create:2024/11/6 10:34
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.获取请求头中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //2.token为空，直接放行
            filterChain.doFilter(request, response);
            return;
        }
        //3.token不为空
        //3.1解析token中的id
        String id;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            id = claims.getSubject();
        } catch (Exception e) {
            //TODO 使用统一异常类封装
            throw new RuntimeException("token非法");
        }
        //4.根据id从redis中获取用户信息
        String key = LOGIN_USER_KEY + id;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("redis中数据为空，用户未登录");
        }
        //5.将用户信息存入SecurityContextHolder
        //TODO获取当前用户权限信息封装到Authentication 直接从LoginUser中获取即可
        //5.1封装用户信息到Authentication

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        //5.2将信息存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //6.放行
        filterChain.doFilter(request,response);
    }
}
