package com.example.familyeducation.utils;

import com.example.familyeducation.entity.Order;
import com.example.familyeducation.mapper.OrderMapper;
import com.example.familyeducation.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 15:11
 **/
@SpringBootTest
public class RedisCacheTest {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void insertListTest(){
        List<Order> orderList = orderMapper.selectList(null);

        for (Order order : orderList) {
            String key = "test:list"+order.getId();
            redisCache.setCacheObject(key,order,2, TimeUnit.MINUTES);
        }
    }
}
