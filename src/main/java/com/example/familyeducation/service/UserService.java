package com.example.familyeducation.service;

import com.example.familyeducation.vo.UserInfoVO;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 9:29
 **/
public interface UserService {
    UserInfoVO selectByUserId(String userId);
}
