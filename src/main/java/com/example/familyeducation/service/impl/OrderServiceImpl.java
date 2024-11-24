package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Order;
import com.example.familyeducation.mapper.OrderMapper;
import com.example.familyeducation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 8:50
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 插入订单数据
     **/
    @Override
    public int insertOrder(Order order) {
        int insert = orderMapper.insert(order);
        return insert;
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 更新订单数据
     **/
    @Override
    public int updateOrder(Order order) {
        int updateById = orderMapper.updateById(order);
        return updateById;
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 查询对应用户订单
     **/
    @Override
    public List<Order> selectMyOrders(QueryWrapper<Order> orderQueryWrapper) {
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        return orderList;
    }

    /**
     * @author 小菜
     * @date  2024/11/24
     * @description 查询所有审核通过的订单
     **/
    @Override
    public List<Order> selectAllOrders(QueryWrapper<Order> orderQueryWrapper) {
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        return orderList;
    }

    /**
     * @author 小菜
     * @date  2024/11/24
     * @description 查询订单相关的教师id
     **/
    @Override
    public Long getTeacherId(QueryWrapper<Order> orderQueryWrapper) {
        Long teacherId = orderMapper.selectOne(orderQueryWrapper).getTeacherId();
        return teacherId;
    }
}
