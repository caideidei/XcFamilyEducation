package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.ParentMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.vo.ParentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:43
 **/
@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParentMapper parentMapper;

    @Override
    public ResponseResult selectAllParents() {
        //1.查询user表中的数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","parent");
        List<User> parents = userMapper.selectList(queryWrapper);
        //2.将user数据封装为ParentVO
        List<ParentVO> parentVOS = parents.stream().map(user -> {
            ParentVO parentVO = new ParentVO();
            parentVO.setUsername(user.getUsername());
            parentVO.setPhoneNumber(user.getPhoneNumber());
            parentVO.setRole(user.getRole());
            parentVO.setStatus(user.getStatus());
            parentVO.setEmail(user.getEmail());
            parentVO.setPicture(user.getPicture());
            return parentVO;
        }).collect(Collectors.toList());
        return  ResponseResult.success("查询成功",parentVOS);
    }
}
