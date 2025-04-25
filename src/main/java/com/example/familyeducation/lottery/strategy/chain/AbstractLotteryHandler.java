package com.example.familyeducation.lottery.strategy.chain;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.LotteryResult;

/**
 * 责任链基类-用于连接责任链
 */
public abstract class AbstractLotteryHandler implements LotteryHandler {
    protected LotteryHandler nextHandler;

    public AbstractLotteryHandler(LotteryHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public LotteryResult handle(LotteryRequest request) {
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return null;
    }
}
