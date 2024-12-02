package com.example.familyeducation.controller;

import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.vo.AdminVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:21
 **/
@RestController
@RequestMapping("/admin")
@Tag(name="管理员接口",description = "管理管理员信息")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/selectAllAdmins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllAdmins(){
        List<AdminVO> adminVOList = adminService.selectAllAdmins();
        if(adminVOList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询成功",adminVOList);
        }
    }

    @PostMapping("/insertAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult insertAdmin(@RequestBody UserDTO userDTO){
        return adminService.insertAdmin(userDTO);
    }

    @PutMapping("/updateAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateAdmin(@RequestBody User user){
        return adminService.updateAdmin(user);
    }

    @DeleteMapping("/deleteAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteAdmin(@RequestParam String phoneNumber){
        return adminService.deleteAdmin(phoneNumber);
    }
}
