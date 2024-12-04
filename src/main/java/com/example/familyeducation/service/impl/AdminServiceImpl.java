package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.AdminMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.vo.AdminVO;
import com.example.familyeducation.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:管理员相关接口
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

    @Autowired
    private RedisCache redisCache;

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 查询所有管理员信息
     **/
    @Override
    public List<AdminVO> selectAllAdmins() {
        return adminMapper.selectAllAdmins();
    }

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 插入新管理员信息
     **/
    @Override
    @Transactional
    public int insertAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }

    /**
     * @author 小菜
     * @date  2024/12/4
     * @description 根据条件查询管理员id
     **/
    @Override
    public Long selectAdminId(QueryWrapper<Admin> adminQueryWrapper) {
        Long id = adminMapper.selectOne(adminQueryWrapper).getId();
        return id;
    }

    /**
     * @author 小菜
     * @date  2024/12/4
     * @description 更新admin
     **/
    @Override
    public int updateAdmin(Admin admin) {
        return adminMapper.updateById(admin);
    }

}
