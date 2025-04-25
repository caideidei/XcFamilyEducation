package com.example.familyeducation.lottery.strategy.chain.handler;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.LotteryResult;
import com.example.familyeducation.lottery.strategy.chain.AbstractLotteryHandler;
import com.example.familyeducation.lottery.strategy.chain.LotteryHandler;
import org.springframework.stereotype.Service;

@Service
public class DefaultLotteryHandler extends AbstractLotteryHandler {

    public DefaultLotteryHandler(LotteryHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public LotteryResult handle(LotteryRequest request) {
        request.setPoolType("DEFAULT");
        return super.handle(request);
    }
}
