package com.example.familyeducation.controller;

import com.example.familyeducation.entity.User;
import com.example.familyeducation.service.LoginService;
import com.example.familyeducation.response.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/5 16:57
 **/

@RestController
@RequestMapping("/user")
@Tag(name="用户管理",description = "用户登录登出")
public class UserController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
