package com.example.familyeducation.service;


import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/10/30 20:44
 **/
public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
