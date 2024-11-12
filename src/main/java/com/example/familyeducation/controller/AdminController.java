package com.example.familyeducation.controller;

import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:21
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/selectAllAdmins")
    public ResponseResult selectAllAdmins(){
        return adminService.selectAllAdmins();
    }
}
