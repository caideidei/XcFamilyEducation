package com.example.familyeducation.handler;

import com.alibaba.fastjson.JSON;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassDescription:授权异常封装
 * @Author:小菜
 * @Create:2024/11/7 18:33
 **/

@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "springSecurity运行时异常：权限不足",null);
        log.error("运行时异常：权限不足：{}",accessDeniedException.getMessage());
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
