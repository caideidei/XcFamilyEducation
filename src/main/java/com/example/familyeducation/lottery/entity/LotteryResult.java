package com.example.familyeducation.lottery.entity;

import lombok.Data;
/**
 * 抽奖结果
 */
@Data
public class LotteryResult {
    private boolean success;
    private String message;

    public LotteryResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
