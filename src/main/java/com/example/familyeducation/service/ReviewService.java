package com.example.familyeducation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.familyeducation.entity.Review;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/21 19:18
 **/
public interface ReviewService extends IService<Review> {
    List<Review> selectReviewsByTeacherId(Long loginUserId);

    List<Review> selectReviewsByParentId(Long loginUserId);
}
