package com.example.familyeducation.controller;

import com.example.familyeducation.entity.User;
import com.example.familyeducation.service.LoginService;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return loginService.register(user);
    }

    @GetMapping("/user-info")
    public ResponseResult selectUserInfo(@RequestHeader("token") String token) throws Exception {
        //1.解析token并获得其中的用户id
        Claims parseJWT = JwtUtil.parseJWT(token);
        String userId = parseJWT.getSubject();
        //2.根据用户id查询头像与用户名并返回
        UserInfoVO userInfoVO = userService.selectByUserId(userId);
        if(userInfoVO==null){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询数据如下：",userInfoVO);
        }
    }

}
