package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.TeacherExamine;
import com.example.familyeducation.mapper.TeacherExamineMapper;
import com.example.familyeducation.service.TeacherExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 14:51
 **/
@Service
public class TeacherExamineImpl implements TeacherExamineService {
    @Autowired
    private TeacherExamineMapper teacherExamineMapper;

    @Override
    public int insert(TeacherExamine teacherExamine) {
        return teacherExamineMapper.insert(teacherExamine);
    }

    @Override
    public int update(TeacherExamine teacherExamine) {
        return teacherExamineMapper.updateById(teacherExamine);
    }

    @Override
    public List<TeacherExamine> selectById(Long teacherId) {
        QueryWrapper<TeacherExamine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        return teacherExamineMapper.selectList(queryWrapper);
    }

    @Override
    public List<TeacherExamine> selectAll() {
        return teacherExamineMapper.selectList(null);
    }

}
