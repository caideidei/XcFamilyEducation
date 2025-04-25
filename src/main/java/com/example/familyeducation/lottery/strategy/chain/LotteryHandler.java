package com.example.familyeducation.lottery.strategy.chain;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.LotteryResult;
/**
 * 抽奖前责任链接口
 */
public interface LotteryHandler {
    LotteryResult handle(LotteryRequest request);
}
