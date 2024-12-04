package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.TeacherMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.vo.TeacherVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:06
 **/
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 查询所有教师的部分信息（从user和teacher中）
     **/
    @Override
    public List<TeacherVO> selectAllTeachers() {
        //1.查询两张表中的数据并封装为TeacherVO
        List<TeacherVO> teacherVOS = teacherMapper.selectAllTeachers();
        //2.返回数据
        return teacherVOS;
    }

    @Override
    public Long selectTeacherId(QueryWrapper<Teacher> teacherQueryWrapper) {
        return teacherMapper.selectOne(teacherQueryWrapper).getId();
    }

    @Override
    public Teacher selectById(QueryWrapper<Teacher> teacherQueryWrapper) {
        return teacherMapper.selectOne(teacherQueryWrapper);
    }

    /**
     * @author 小菜
     * @date  2024/12/4
     * @description 修改teacher表的信息
     **/
    @Override
    public int update(Teacher teacher) {
        return teacherMapper.updateById(teacher);
    }

}
