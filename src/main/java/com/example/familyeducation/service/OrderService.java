package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Order;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 8:50
 **/
public interface OrderService {
    int insertOrder(Order order);
    int updateStatusToPassed(Order order);


    List<Order> selectMyOrders(QueryWrapper<Order> orderQueryWrapper);
}
