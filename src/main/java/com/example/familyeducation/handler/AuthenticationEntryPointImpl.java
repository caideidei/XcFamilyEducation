package com.example.familyeducation.handler;

import com.alibaba.fastjson.JSON;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassDescription:认证过程异常封装
 * @Author:小菜
 * @Create:2024/11/7 18:36
 **/
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "springSecurity运行时异常：用户认证失败",null);
        log.error("运行时异常：认证失败：{}",authException.getMessage());
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
