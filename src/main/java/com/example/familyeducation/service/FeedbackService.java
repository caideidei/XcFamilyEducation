package com.example.familyeducation.service;

import com.example.familyeducation.entity.Feedback;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.vo.FeedbackVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 16:24
 **/
public interface FeedbackService {
    int insertFeedback(Feedback feedback);

    List<FeedbackVO> selectAllFeedBacks();

    int updateFeedback(Feedback feedback);

    int deleteFeedback(Long id);

    Long selectSenderIdById(Long id);
}
