package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.vo.TeacherVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper extends BaseMapper<Teacher> {

    @Select("select t.id,user_id,username,real_name,phone_number,\n" +
            "       email,role,status,created_at,picture,qualification,\n" +
            "      intro,official_teacher,subjects\n" +
            "from user u join teacher t on u.id = t.user_id;")
    List<TeacherVO> selectAllTeachers();
}
