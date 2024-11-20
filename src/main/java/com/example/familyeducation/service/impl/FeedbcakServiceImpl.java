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
    public ResponseResult insertFeedback(Feedback feedback) {
        //1.获取当前登录用户id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        //2.封装feedback对象
        feedback.setSenderId(loginUserId);
        //3.传入数据库
        int insertFeedbackNumber = feedbackMapper.insert(feedback);
        //4.判断传入是否成功并返回信息
        if(insertFeedbackNumber==0){
            return ResponseResult.error("新增反馈失败");
        }else{
            return ResponseResult.success("新增反馈成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/19
     * @description 查询所有未被banned用户的反馈信息
     **/
    @Override
    public ResponseResult selectAllFeedBacks() {
        //查询feedback的信息与user表中的相关用户信息
        List<FeedbackVO> feedbackVOS = feedbackMapper.selectAllFeedbacks();
        //返回数据
        return ResponseResult.success("查询成功",feedbackVOS);
    }

    @Override
    public ResponseResult updateFeedback(Feedback feedback) {
        int updateFeedbackNumber = 0;
        //1.获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        //2.判断是否是修改自己的反馈
        if(loginUserId!=feedback.getSenderId()){
            //2.1修改别人的反馈（登录用户与修改的反馈信息用户不同），报错
            return ResponseResult.error("无法修改别人的反馈信息");
        }else{
            //3.修改自己的反馈，根据传入的feedback对象直接修改数据库表
            //时间修改为当前时间
            feedback.setCreatedAt(LocalDateTime.now());
            updateFeedbackNumber = feedbackMapper.updateById(feedback);
        }
        //根据更新的条数返回前端信息
        if(updateFeedbackNumber==0){
            return ResponseResult.error("更新失败");
        }else{
            return ResponseResult.success("更新成功",null);
        }
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
