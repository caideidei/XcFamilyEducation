package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public UserInfoVO selectUserInfoByUserId(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User user = userMapper.selectOne(queryWrapper);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfoVO);
        return userInfoVO;
    }

    @Override
    public List<User> selectByPhone(String phoneNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList;
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User selectOneByPhone(String phoneNumber) {
        QueryWrapper<User> queryRegisterWrapper = new QueryWrapper<>();
        queryRegisterWrapper.eq("phone_number",phoneNumber);
        User registerUser = userMapper.selectOne(queryRegisterWrapper);
        return registerUser;
    }

    @Override
    public List<User> selectByPhoneAndId(String phoneNumber, Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber).ne("id",id);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public int updateById(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public String selectUserStatusByUserId(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return userMapper.selectOne(queryWrapper).getStatus();
    }

    @Override
    public int deleteUserById(Long userId) {
        return userMapper.deleteById(userId);
    }

    @Override
    public User selectUserByUserId(String userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return userMapper.selectOne(queryWrapper);
    }


}
