package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 9:29
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserInfoVO selectByUserId(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User user = userMapper.selectOne(queryWrapper);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfoVO);
        return userInfoVO;
    }
}
