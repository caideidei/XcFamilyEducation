package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.vo.TeacherVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper extends BaseMapper<Teacher> {

    @Select("select username,phone_number,subjects,qualification,intro,email,picture,role,status\n" +
            "from teacher t join user u on t.user_id = u.id;")
    List<TeacherVO> selectAllTeachers();
}
