package com.example.familyeducation.lottery.strategy.rule;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.Prize;

/**
 * 规则树基类-用于连接节点
 */
public abstract class AbstractLotteryRuleNode implements LotteryRuleNode {
    protected LotteryRuleNode nextNode;

    public AbstractLotteryRuleNode(LotteryRuleNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean apply(LotteryRequest request, Prize prize) {
        if (nextNode != null) {
            return nextNode.apply(request, prize);
        }
        return true;
    }
}
