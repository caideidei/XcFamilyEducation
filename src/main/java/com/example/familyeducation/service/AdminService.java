package com.example.familyeducation.service;

import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:23
 **/
public interface AdminService {
    ResponseResult selectAllAdmins();

    ResponseResult insertAdmin(UserDTO userDTO);

    ResponseResult updateAdmin(User user);

    ResponseResult deleteAdmin(String phoneNumber);
}
