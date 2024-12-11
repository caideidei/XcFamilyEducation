package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.familyeducation.entity.Review;
import com.example.familyeducation.mapper.ReviewMapper;
import com.example.familyeducation.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/21 19:18
 **/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Override
    public List<Review> selectReviewsByTeacherId(Long loginUserId) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",loginUserId);
        return reviewMapper.selectList(queryWrapper);
    }

    @Override
    public List<Review> selectReviewsByParentId(Long loginUserId) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",loginUserId);
        return reviewMapper.selectList(queryWrapper);
    }
}
