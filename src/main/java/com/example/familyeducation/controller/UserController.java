package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserPhoneCodeDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.service.*;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.vo.AdminVO;
import com.example.familyeducation.vo.ParentVO;
import com.example.familyeducation.vo.TeacherVO;
import com.example.familyeducation.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private AdminService adminService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ParentService parentService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }


    @PostMapping("/loginByPhoneCode")
    public ResponseResult loginByPhoneCode(@RequestBody UserPhoneCodeDTO user){
        return loginService.loginByPhoneNumber(user);
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
        UserInfoVO userInfoVO = userService.selectUserInfoByUserId(userId);
        if(userInfoVO==null){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询数据如下：",userInfoVO);
        }
    }

    @GetMapping("/selectUser")
    public ResponseResult selectUser(@RequestHeader("token") String token) throws Exception {
        //1.解析token中的id
        Claims parseJWT = JwtUtil.parseJWT(token);
        String userId = parseJWT.getSubject();
        //2.根据id查找用户信息并返回
        User user = userService.selectUserByUserId(userId);
        if(user.getRole().equals("admin")){
            AdminVO adminVO = new AdminVO();
            BeanUtils.copyProperties(user,adminVO);
            Admin admin = adminService.selectAdminByUserId(user.getId());
            BeanUtils.copyProperties(admin,adminVO);
            return ResponseResult.success("成功查询数据",adminVO);
        } else if (user.getRole().equals("teacher")) {
            TeacherVO teacherVO = new TeacherVO();
            BeanUtils.copyProperties(user,teacherVO);
            QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>().eq("user_id", userId);
            Teacher teacher = teacherService.selectById(queryWrapper);
            BeanUtils.copyProperties(teacher,teacherVO);
            return ResponseResult.success("成功查询数据",teacherVO);
        }else{
            ParentVO parentVO = new ParentVO();
            BeanUtils.copyProperties(user,parentVO);
            Parent parent = parentService.selectParentByUserId(user.getId());
            BeanUtils.copyProperties(parent,parentVO);
            return ResponseResult.success("成功查询数据",parentVO);
        }
    }

}
