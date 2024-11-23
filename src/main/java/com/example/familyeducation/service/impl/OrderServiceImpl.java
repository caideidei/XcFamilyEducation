package com.example.familyeducation.service.impl;

import com.example.familyeducation.entity.Order;
import com.example.familyeducation.mapper.OrderMapper;
import com.example.familyeducation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 8:50
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int insertOrder(Order order) {
        int insert = orderMapper.insert(order);
        return insert;
    }
}
