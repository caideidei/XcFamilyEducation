package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.TeacherExamine;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 14:51
 **/
public interface TeacherExamineService {
    int insert(TeacherExamine teacherExamine);

    int update(TeacherExamine teacherExamine);

    List<TeacherExamine> selectById(Long teacherId);

    List<TeacherExamine> selectAll();
}
