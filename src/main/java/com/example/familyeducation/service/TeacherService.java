package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.vo.TeacherVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:06
 **/
public interface TeacherService {
    List<TeacherVO> selectAllTeachers();

    ResponseResult updateTeacherStatus(TeacherDTO teacherDTO);

    Long selectTeacherId(QueryWrapper<Teacher> teacherQueryWrapper);

    Teacher selectById(QueryWrapper<Teacher> teacherQueryWrapper);

    int update(Teacher teacher);
}
