package com.example.familyeducation.lottery.service;

import com.example.familyeducation.lottery.entity.LotteryRequest;
import com.example.familyeducation.lottery.entity.LotteryResult;
import com.example.familyeducation.lottery.entity.Prize;
import com.example.familyeducation.lottery.strategy.chain.LotteryHandler;
import com.example.familyeducation.lottery.strategy.rule.LotteryRuleService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class LotteryService {

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private LotteryHandler lotteryHandlerChain;

    @Autowired
    private LotteryRuleService lotteryRuleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedissonClient redissonClient;

    public LotteryResult handleLottery(LotteryRequest request) {
        // 责任链判断
        LotteryResult result = lotteryHandlerChain.handle(request);
        if (!result.isSuccess()) {
            return result; // 失败返回
        }

        // 根据 poolType 获取奖池并抽奖
        Prize prize = prizeService.drawPrize(request.getPoolType());

        // 规则树判断
        boolean isValid = lotteryRuleService.validateRules(request, prize);
        if (!isValid) {
            return new LotteryResult(false, "抽奖失败，未满足规则");
        }

        // 扣减库存和更新记录
        updateStockAndRecord(prize);

        return new LotteryResult(true, "恭喜中奖！");
    }

    //方案一：lua脚本扣减Redis库存再发送异步消息mq扣减MySQL库存
    private void updateStockAndRecord(Prize prize) {
        // 先扣减 Redis 中的库存
        if (!decrStockByLua(prize.getId().toString())) {
            System.out.println("返回积分");
        }

        // 异步通过 MQ 更新 MySQL 库存和中奖记录
        rabbitTemplate.convertAndSend("lottery_exchange", "lottery.success", prize);
    }

    // 执行库存扣减的Lua脚本（判断大于0，扣减库存）
    public boolean decrStockByLua(String prizeKey) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(
                "local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                        "if not stock or stock <= 0 then\n" +
                        "    return -1\n" +
                        "end\n" +
                        "redis.call('decr', KEYS[1])\n" +
                        "return 1"
        );
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(redisScript, Collections.singletonList(prizeKey));
        return result != null && result == 1L;
    }

    //方案二：redission加锁/setnx:redission相当于是setnx的升级版，提供了看门狗机制自动续期
    public void updateStockAndRecord2(Prize productId){
        RLock lock = redissonClient.getLock("lock:product:" + productId);
        boolean locked = false;
        try {
            // 尝试获取锁：最多等 5 秒，锁持有 30 秒后自动释放
            locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new RuntimeException("当前系统繁忙，请稍后重试");
            }

            // —— 以下为你的扣库存逻辑 ——
            // 1）先在 Redis 扣减
            // Long stock = redis.opsForValue().decrement("stock:" + productId, amount);
            // if (stock < 0) { throw new RuntimeException("库存不足"); }
            //
            // 2）再同步（或异步）更新到 MySQL
            // productMapper.updateStock(productId, amount);
            // ——————————————

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断", e);
        } finally {
            // 释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
