package com.example.familyeducation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassDescription: 课程订单表
 * @Author:小菜
 * @Create:2025/2/28 20:29
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseOrder {
    private String orderId;
    private String userId;
    private String courseId;
    private BigDecimal price;
    private String status;
    private String payUrl;
    private Date orderTime;
}
