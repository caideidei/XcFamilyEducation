package com.example.familyeducation.lottery.strategy.rule.node;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.Prize;
import com.example.familyeducation.lottery.strategy.rule.AbstractLotteryRuleNode;
import com.example.familyeducation.lottery.strategy.rule.LotteryRuleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockLotteryRuleNode extends AbstractLotteryRuleNode {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public StockLotteryRuleNode(LotteryRuleNode nextNode) {
        super(nextNode);
    }

    @Override
    public boolean apply(LotteryRequest request, Prize prize) {
        String stockKey = "lottery:stock:" + prize.getId();
        Long stock = Long.valueOf(redisTemplate.opsForValue().get(stockKey));

        if (stock == null || stock <= 0) {
            return false;  // 库存不足
        }
        return super.apply(request, prize);
    }
}
