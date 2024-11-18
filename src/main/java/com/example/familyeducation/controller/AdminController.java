package com.example.familyeducation.controller;

import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllAdmins(){
        return adminService.selectAllAdmins();
    }

    @PostMapping("/insertAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult insertAdmin(@RequestBody UserDTO userDTO){
        return adminService.insertAdmin(userDTO);
    }

    @PostMapping("/updateAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateAdmin(@RequestBody User user){
        return adminService.updateAdmin(user);
    }
}
