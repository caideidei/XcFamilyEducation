package com.example.familyeducation.lottery.strategy.rule;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryRuleService {

    @Autowired
    private LotteryRuleNode ruleChain;

    public boolean validateRules(LotteryRequest request, Prize prize) {
        return ruleChain.apply(request, prize);
    }
}
