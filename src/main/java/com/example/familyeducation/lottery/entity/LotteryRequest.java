package com.example.familyeducation.lottery.entity;

import lombok.Data;
/**
 * 抽奖请求
 */
@Data
public class LotteryRequest {
    private Long userId;
    private String poolType; // VIP、DEFAULT等
}
