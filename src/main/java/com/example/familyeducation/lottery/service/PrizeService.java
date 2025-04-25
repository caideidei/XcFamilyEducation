package com.example.familyeducation.lottery.service;

import com.example.familyeducation.lottery.entity.Prize;
import com.example.familyeducation.lottery.mapper.PrizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 奖品预热service-将奖品数据加载到redis中（普通奖池/vip奖池）
 */
@Service
public class PrizeService {

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String POOL_COMMON = "lottery:pool:common";
    private static final String POOL_VIP = "lottery:pool:vip";

    //装配奖品池方法
    public void preloadPrizePool() {
        List<Prize> prizes = prizeMapper.getAllPrizes();
        redisTemplate.delete(POOL_COMMON);
        redisTemplate.delete(POOL_VIP);

        for (Prize prize : prizes) {
            String key = prize.getLevel() == 2 ? POOL_VIP : POOL_COMMON;
            redisTemplate.opsForZSet().add(key, prize.getId().toString(), prize.getProbability());
        }
    }

    //抽奖方法
    public Prize drawPrize(String poolType) {
        // 从 Redis 的 ZSet 中按照概率抽奖
        // 例如：使用随机分数查询奖品
        double randomScore = ThreadLocalRandom.current().nextDouble(0, 100);
        Set<String> prizeIds = redisTemplate.opsForZSet().rangeByScore("lottery:pool:" + poolType, 0, randomScore, 0, 1);
        if (prizeIds.isEmpty()) {
            return null;
        }
        // 查询数据库获取完整奖品信息
        Long prizeId = Long.valueOf(prizeIds.iterator().next());
        return prizeMapper.selectById(prizeId);
    }
}
