package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.vo.AdminVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:23
 **/
public interface AdminService {
    List<AdminVO> selectAllAdmins();

    ResponseResult insertAdmin(UserDTO userDTO);

    ResponseResult updateAdmin(User user);

    ResponseResult deleteAdmin(String phoneNumber);

    Long selectAdminId(QueryWrapper<Admin> adminQueryWrapper);
}
