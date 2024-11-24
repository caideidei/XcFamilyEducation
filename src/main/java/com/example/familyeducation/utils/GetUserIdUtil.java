package com.example.familyeducation.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 10:52
 **/
@Component
public class GetUserIdUtil {
    @Autowired
    private ParentService parentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminService adminService;

    public Long getParentId(){
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Parent> parentQueryWrapper = new QueryWrapper<>();
        parentQueryWrapper.eq("user_id",loginUserId);
        Long parentId = parentService.selectParentId(parentQueryWrapper);
        return parentId;
    }

    public Long getTeacherId(){
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("user_id",loginUserId);
        Long teacherId = teacherService.selectTeacherId(teacherQueryWrapper);
        return teacherId;
    }

    public Long getAdminId(){
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("user_id",loginUserId);
        Long adminId = adminService.selectAdminId(adminQueryWrapper);
        return adminId;
    }

}
