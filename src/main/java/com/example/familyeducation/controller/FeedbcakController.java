package com.example.familyeducation.controller;

import com.example.familyeducation.entity.Feedback;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 16:25
 **/
@RestController
@RequestMapping("/feedback")
@Tag(name="反馈接口",description = "管理用户对系统的反馈信息")
public class FeedbcakController {
    @Autowired
    private FeedbackService feedbcakService;

    @PostMapping("/insertFeedback")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult insertFeedback(@RequestBody Feedback feedback){
        return feedbcakService.insertFeedback(feedback);
    }

    @GetMapping("/selectAllFeedbacks")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllFeedbacks(){
        return feedbcakService.selectAllFeedBacks();
    }

    @PutMapping("/updateFeedback")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult updateFeedback(@RequestBody Feedback feedback){
        return feedbcakService.updateFeedback(feedback);
    }

    @DeleteMapping("/deleteFeedback")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult deleteFeedback(@RequestBody Feedback feedback){
        return feedbcakService.deleteFeedback(feedback);
    }

}
