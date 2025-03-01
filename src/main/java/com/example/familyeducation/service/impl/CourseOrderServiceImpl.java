package com.example.familyeducation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.constants.CourseOrderConstants;
import com.example.familyeducation.dto.PayOrderRes;
import com.example.familyeducation.entity.Course;
import com.example.familyeducation.entity.CourseOrder;
import com.example.familyeducation.mapper.CourseMapper;
import com.example.familyeducation.mapper.CourseOrderMapper;
import com.example.familyeducation.service.CourseOrderService;
import com.example.familyeducation.service.mq.OrderTimeoutProducer;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassDescription: 课程购买实现类
 * @Author:小菜
 * @Create:2025/2/28 20:39
 **/
@Service
public class CourseOrderServiceImpl implements CourseOrderService {

    @Value("${alipay.notify_url}")
    private String notifyUrl;

    @Value("${alipay.return_url}")
    private String returnUrl;

    @Resource
    private CourseOrderMapper courseOrderMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private AlipayClient alipayClient;

    @Autowired
    private OrderTimeoutProducer timeoutProducer;

    @Override
    //创建课程用户订单
    public PayOrderRes createOrder(String courseId, String userId) throws AlipayApiException {
        //1.查询用户是否有订单
        QueryWrapper<CourseOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course",courseId).eq("userId",userId);
        CourseOrder courseOrder = courseOrderMapper.selectOne(queryWrapper);
        if(null!=courseOrder&&courseOrder.getStatus().equals(CourseOrderConstants.WAIT)){
            //1.1存在未支付订单,返回未支付订单
            return PayOrderRes.builder()
                    .orderId(courseOrder.getOrderId())
                    .payUrl(courseOrder.getPayUrl()).build();
        }
        //2.创建订单并存入数据库
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        Course course = courseMapper.selectOne(courseQueryWrapper.eq("courseId", courseId));
        String orderId = RandomStringUtils.randomNumeric(16);
        CourseOrder newCourseOrder = CourseOrder.builder()
                .orderId(orderId)
                .userId(userId)
                .courseId(courseId)
                .price(course.getPrice())
                .status(CourseOrderConstants.CREATE)
                .orderTime(new Date()).build();
        courseOrderMapper.insert(newCourseOrder);
        //3.创建支付宝请求
        CourseOrder courseOrder1 = doPrepayOrder(orderId,course.getPrice(),course.getCourseName());

        //4.使用mq延迟队列来关闭超时订单
        long timeout = System.currentTimeMillis() + 30 * 60 * 1000;  // 30分钟后超时
        timeoutProducer.sendTimeoutOrderMessage(orderId,timeout);

        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl(courseOrder1.getPayUrl())
                .build();
    }

    //创建支付宝订单
    private CourseOrder doPrepayOrder(String orderId, BigDecimal price, String courseName) throws AlipayApiException {
        //创建支付宝请求对象
        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        //设置回调方法和路径
        payRequest.setNotifyUrl(notifyUrl);
        payRequest.setReturnUrl(returnUrl);

        //生成支付宝表单
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no",orderId);
        bizContent.put("total_amount",price.toString());
        bizContent.put("subject",courseName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        payRequest.setBizContent(bizContent.toString());
        String form = alipayClient.pageExecute(payRequest).getBody();

        //更新课程用户表并返回结果
        CourseOrder courseOrder = CourseOrder.builder()
                .orderId(orderId)
                .payUrl(form)
                .status(CourseOrderConstants.WAIT).build();

        courseOrderMapper.updateById(courseOrder);

        return courseOrder;
    }

    //回调成功修改数据库属性
    @Override
    public void changeOrderPaySuccess(String orderId) {
        CourseOrder courseOrder = CourseOrder.builder()
                .orderId(orderId)
                .status(CourseOrderConstants.SUCCESS)
                .build();
        courseOrderMapper.updateById(courseOrder);
    }

    //关单
    @Override
    public void changeOrderClose(String orderId) {
        CourseOrder courseOrder = CourseOrder.builder()
                .orderId(orderId)
                .status(CourseOrderConstants.FAIL).build();
        courseOrderMapper.updateById(courseOrder);
    }
}
