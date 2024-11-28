package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.vo.ParentVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ParentMapper extends BaseMapper<Parent> {

    @Select("select  p.id,user_id,username,real_name,phone_number\n" +
            "            ,picture,email,role,status,u.created_at\n" +
            "from user u join parent p on u.id = p.user_id")
    public List<ParentVO> selectAllParents();

}
