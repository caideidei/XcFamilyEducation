package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.vo.AdminVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select a.id,user_id,username,real_name,phone_number,picture,email,role,status,u.created_at \n" +
            "from user  u join admin a on u.id = a.user_id")
    public List<AdminVO> selectAllAdmins();
}
