package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:06
 **/
public interface TeacherService {
    ResponseResult selectAllTeachers();

    ResponseResult updateTeacher(TeacherDTO teacherDTO);

    ResponseResult updateTeacherStatus(TeacherDTO teacherDTO);

    Long selectTeacherId(QueryWrapper<Teacher> teacherQueryWrapper);
}
