package com.example.familyeducation.lottery.strategy.rule;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.Prize;

/**
 * 规则树接口
 */
public interface LotteryRuleNode {
    boolean apply(LotteryRequest request, Prize prize);
}
