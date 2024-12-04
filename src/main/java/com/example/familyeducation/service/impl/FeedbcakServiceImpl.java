package com.example.familyeducation.service.impl;

import com.example.familyeducation.entity.Feedback;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.mapper.FeedbackMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.FeedbackService;
import com.example.familyeducation.vo.FeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 16:25
 **/
@Service
public class FeedbcakServiceImpl implements FeedbackService{
    @Autowired
    private FeedbackMapper feedbackMapper;

    /**
     * @author 小菜
     * @date  2024/11/19
     * @description 新增反馈信息
     **/
    @Override
    public int insertFeedback(Feedback feedback) {
        return feedbackMapper.insert(feedback);
    }

    /**
     * @author 小菜
     * @date  2024/11/19
     * @description 查询所有未被banned用户的反馈信息
     **/
    @Override
    public List<FeedbackVO> selectAllFeedBacks() {
        return feedbackMapper.selectAllFeedbacks();
    }

    @Override
    public int updateFeedback(Feedback feedback) {
        return feedbackMapper.updateById(feedback);
    }

    @Override
    public ResponseResult deleteFeedback(Feedback feedback) {
        int deleteFeedbackNumber = 0;
        //1.获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        //2.判断是否删除的是自己的反馈
        if(loginUserId!=feedback.getSenderId()){
             //2.1删除别人的反馈，报错
            return ResponseResult.error("无法删除别人的反馈信息");
        }else{
            //3.删除自己的反馈，直接从数据库中进行删除
            deleteFeedbackNumber = feedbackMapper.deleteById(feedback);
        }
        if(deleteFeedbackNumber==0){
            return ResponseResult.error("删除失败");
        }else{
            return ResponseResult.success("删除成功",null);
        }
    }
}
