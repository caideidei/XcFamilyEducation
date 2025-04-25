package com.example.familyeducation.lottery.strategy.chain.handler;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.LotteryResult;
import com.example.familyeducation.lottery.strategy.chain.AbstractLotteryHandler;
import com.example.familyeducation.lottery.strategy.chain.LotteryHandler;
import com.example.familyeducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VipLotteryHandler extends AbstractLotteryHandler {

    @Autowired
    private UserService userService;

    public VipLotteryHandler(LotteryHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public LotteryResult handle(LotteryRequest request) {
        if (true) {//userService.isVip(request.getUserId())//判断是否为VIP
            request.setPoolType("VIP");
            return super.handle(request);
        }
        return super.handle(request);
    }
}
