package com.example.familyeducation.filter;

import com.alibaba.fastjson.JSON;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.exception.BusinessException;
import com.example.familyeducation.exception.GlobalExceptionHandler;
import com.example.familyeducation.handler.AuthenticationEntryPointImpl;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.CommonService;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @Autowired
    private CommonService commonService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
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
            //3.2token过期，检查refreshToken是否过期
            String refreshToken = request.getHeader("refreshToken");
            if(StringUtils.hasText(refreshToken)){
                try {
                    //存在refreshToken去调用方法检验refreshToken并生成新token
                    id = JwtUtil.parseJWT(refreshToken).getSubject();
                } catch (Exception ex) {
                    ResponseResult<Object> responseResult = new ResponseResult(401, "请重新登录",null);
                    String s = JSON.toJSONString(responseResult);
                    response.getWriter().write(s);
                    return;
                }
            }else{
                //不存在refreshToken直接报错
                ResponseResult<Object> responseResult = new ResponseResult(401, "请重新登录",null);
                String s = JSON.toJSONString(responseResult);
                response.getWriter().write(s);
                return;
            }
        }

        //4.根据id从redis中获取用户信息
        String key = LOGIN_USER_KEY + id;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if(Objects.isNull(loginUser)){
            ResponseResult<Object> responseResult = new ResponseResult(401, "请重新登录",null);
            String s = JSON.toJSONString(responseResult);
            response.getWriter().write(s);
            return;
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
