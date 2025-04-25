package com.example.familyeducation.lottery.strategy.rule.node;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.Prize;
import com.example.familyeducation.lottery.strategy.rule.AbstractLotteryRuleNode;
import com.example.familyeducation.lottery.strategy.rule.LotteryRuleNode;
import com.example.familyeducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLotteryCountRuleNode extends AbstractLotteryRuleNode {

    @Autowired
    private UserService userService;

    public UserLotteryCountRuleNode(LotteryRuleNode nextNode) {
        super(nextNode);
    }

    @Override
    public boolean apply(LotteryRequest request, Prize prize) {
        //获取用户中奖次数  userService.getLotteryCount(request.getUserId(), prize.getId());
        int count = 0;
        if (count >= 2) {
            return false;  // 达到中奖次数上限
        }
        return super.apply(request, prize);
    }
}
