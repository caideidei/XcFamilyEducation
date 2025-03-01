package com.example.familyeducation.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassDescription: 课程表
 * @Author:小菜
 * @Create:2025/2/28 20:27
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private String courseId;
    private String courseName;
    private BigDecimal price;
}
