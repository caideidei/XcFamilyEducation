package com.example.familyeducation.service.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/4 19:06
 **/

//这里继承的是security中的一个默认接口，重写其中的查询用户方法
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO根据用户查询权限信息 添加到LoginUser中
        //上面的user信息中已经包含了权限信息，但是我们还是要单独把权限提出来

        List<String> list = new ArrayList<>();//这里list就是LoginUser中的redisAuthorities
        String role = user.getRole();
        if(role.equals("admin")){
            list.add("ROLE_ADMIN");//注意这里添加自定义权限时要加前缀ROLE_，SpringSecurity会默认根据ROLE_去查找权限
        } else if (role.equals("teacher")) {
            list.add("ROLE_TEACHER");
        }
        //封装成UserDetails对象返回
        return new LoginUser(user,list);
    }
}
