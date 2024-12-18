package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Review;
import com.example.familyeducation.mapper.ParentMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ReviewService;
import com.example.familyeducation.utils.GetUserIdUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/21 19:19
 **/
@RestController
@RequestMapping("/review")
@Tag(name="评论接口",description ="管理评论信息" )
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private GetUserIdUtil getUserIdUtil;

    @GetMapping("/selectAllReviews")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllReviews(){
        List<Review> reviewList = reviewService.list();
        if(reviewList==null){
            return ResponseResult.success("查询结果为空",null);
        }else{
            return ResponseResult.success("查询成功",reviewList);
        }
    }

    @PostMapping("/insertReview")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult insertReview(@RequestBody Review review){
        Long parentId = getUserIdUtil.getParentId();
        review.setParentId(parentId);
        review.setCreatedAt(LocalDateTime.now());
        boolean insertReviewNumber = reviewService.save(review);
        if(insertReviewNumber==false){
            return ResponseResult.error("新增失败");
        }else{
            return ResponseResult.success("新增成功",null);
        }
    }

    @PutMapping("/updateReview")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult updateReview(@RequestBody Review review){
        boolean updateReview = false;
        //1.判断传入parent_id与当前登录id是否相同
        Long parentId = review.getParentId();
        Long loginParentId = getUserIdUtil.getParentId();
        if(parentId!=loginParentId){
            return ResponseResult.error("无法更新别人的评论");
        }else{
            review.setCreatedAt(LocalDateTime.now());
            updateReview = reviewService.updateById(review);
        }
        if(updateReview==false){
            return ResponseResult.error("更新失败");
        }else{
            return ResponseResult.success("更新成功",null);
        }
    }

    @DeleteMapping("/deleteReview")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult deleteReview(@RequestBody Review review){
        boolean deleteReview = false;
        //1.判断传入parent_id与当前登录id是否相同
        Long parentId = review.getParentId();
        Long loginParentId = getUserIdUtil.getParentId();
        if(parentId!=loginParentId){
            return ResponseResult.error("无法删除别人的评论");
        }else{
            deleteReview = reviewService.removeById(review);
        }
        if(deleteReview==false){
            return ResponseResult.error("删除失败");
        }else{
            return ResponseResult.success("删除成功",null);
        }
    }

    @GetMapping("/selectMyReviews")
    @PreAuthorize("hasAnyRole('TEACHER','PARENT')")
    public ResponseResult selectMyReviews(){
        List<Review> reviewList;
        //1.从SecurityContextHolder中获取当前用户的相关信息
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = loginUser.getUser().getRole();
        //2.根据用户角色查询相关信息并返回
        if(role.equals("teacher")){
            Long teacherId = getUserIdUtil.getTeacherId();
            reviewList = reviewService.selectReviewsByTeacherId(teacherId);
        }else{
            Long parentId = getUserIdUtil.getParentId();
            reviewList = reviewService.selectReviewsByParentId(parentId);
        }
        //3.返回信息给前端
        if(reviewList==null){
            return ResponseResult.error("查询数据为空");
        }else{
            return ResponseResult.success("查询数据成功",reviewList);
        }

    }

}
