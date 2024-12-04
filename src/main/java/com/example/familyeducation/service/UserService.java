package com.example.familyeducation.service;

import com.example.familyeducation.entity.User;
import com.example.familyeducation.vo.UserInfoVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 9:29
 **/
public interface UserService {
    UserInfoVO selectByUserId(String userId);

    List<User> selectByPhone(String phoneNumber);

    int insertUser(User user);

    User selectOneByPhone(String phoneNumber);

    List<User> selectByPhoneAndId(String phoneNumber,Long id);

    int updateById(User user);

    String selectUserStatusByUserId(Long userId);

    int  deleteUserById(Long userId);
}
