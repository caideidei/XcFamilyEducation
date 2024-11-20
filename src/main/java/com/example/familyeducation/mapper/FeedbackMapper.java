package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.familyeducation.entity.Feedback;
import com.example.familyeducation.vo.FeedbackVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FeedbackMapper extends BaseMapper<Feedback> {
    @Select("select f.id,f.sender_id,u.username,u.role,u.picture,f.message,f.created_at\n" +
            "from feedback f join user u on f.sender_id = u.id\n" +
            "where u.status!='banned'\n" +
            "order by f.created_at")
    List<FeedbackVO> selectAllFeedbacks();
}
