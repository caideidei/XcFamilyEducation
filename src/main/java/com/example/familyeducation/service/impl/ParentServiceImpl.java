package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.ParentMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.vo.ParentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ParentVO> selectAllParents() {
        return parentMapper.selectAllParents();
    }

    @Override
    public int updateParent(Parent parent) {
        return parentMapper.updateById(parent);
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 根据条件查询parent_id
     **/
    @Override
    public Long selectParentId(QueryWrapper<Parent> parentQueryWrapper) {
        Long id = parentMapper.selectOne(parentQueryWrapper).getId();
        return id;
    }
}
