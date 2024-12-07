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
    public int deleteFeedback(Long id) {
        return feedbackMapper.deleteById(id);
    }

    @Override
    public Long selectSenderIdById(Long id) {
        return feedbackMapper.selectById(id).getSenderId();
    }

    @Override
    public List<FeedbackVO> selectMyFeedbacks(Long userId) {
        return feedbackMapper.selectMyFeedbacks(userId);
    }
}
