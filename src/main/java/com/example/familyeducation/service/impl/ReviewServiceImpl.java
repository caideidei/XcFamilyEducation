package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.familyeducation.entity.Review;
import com.example.familyeducation.mapper.ReviewMapper;
import com.example.familyeducation.service.ReviewService;
import org.springframework.stereotype.Service;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/21 19:18
 **/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
}
