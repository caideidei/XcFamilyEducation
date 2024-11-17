package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.AdminMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:23
 **/
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult selectAllAdmins() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","admin");
        List<User> admins = userMapper.selectList(queryWrapper);

        //将查询到的数据封装为UserVO
        List<UserVO> userVOS = admins.stream().map(user -> {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setPhoneNumber(user.getPhoneNumber());
            userVO.setRole(user.getRole());
            userVO.setStatus(user.getStatus());
            userVO.setCreatedAt(user.getCreatedAt());
            userVO.setEmail(user.getEmail());
            userVO.setPicture(user.getPicture());
            return userVO;
        }).collect(Collectors.toList());
        return new ResponseResult(200,"查询成功",userVOS);
    }

    @Override
    @Transactional
    public ResponseResult insertAdmin(UserDTO userDTO) {
        int insertAdminNumber = 0;//用来判断后续数据是否插入成功
        //1.封装user数据
        //1.1创建一个User对象并将userDTO中数据传给user
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        //1.2添加user其他数据
        //初始密码是手机号
        String phoneNumber = userDTO.getPhoneNumber();
        String password = passwordEncoder.encode(phoneNumber);
        user.setPassword(password);
        user.setRole("admin");
        //2.判断手机号是否已被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        List<User> userList = userMapper.selectList(queryWrapper);
        if(!userList.isEmpty()){
            //2.1已被注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //3.未注册则将数据插入数据库
            // 3.1先插入user表中
            userMapper.insert(user);
            //3.2再获取user表中的数据并封装admin对象
            QueryWrapper<User> queryRegisterWrapper = new QueryWrapper<>();
            queryRegisterWrapper.eq("phone_number",phoneNumber);
            User registerUser = userMapper.selectOne(queryRegisterWrapper);
            Long registerUserId = registerUser.getId();
            LocalDateTime createdAt = registerUser.getCreatedAt();

            Admin admin = new Admin();
            admin.setUserId(registerUserId);
            admin.setCreatedAt(createdAt);
            insertAdminNumber = adminMapper.insert(admin);
        }
        //4.判断插入是否成功并返回信息
        if(insertAdminNumber==0){
            return ResponseResult.error("新增管理员失败");
        }else{
            return ResponseResult.success("新增管理员成功",null);
        }

    }
}
