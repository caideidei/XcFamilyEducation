package com.example.familyeducation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.familyeducation.dto.PayOrderRes;
import com.example.familyeducation.entity.CourseOrder;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.CourseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2025/2/28 20:32
 **/
@Slf4j
@RestController
@RequestMapping("/courseOrder/")
public class CourseOrderController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private CourseOrderService courseOrderService;

    /**
     * @description: 课程下单接口
     * @date: 2025/3/1 11:20
     * @param: courseOrder
     * @return: com.example.familyeducation.response.ResponseResult<java.lang.String>
    **/
    @PostMapping("/create_courseOrder")
    public ResponseResult<String> createPayOrder(@RequestBody CourseOrder courseOrder){
        try{
            //下单
            PayOrderRes payOrderRes = courseOrderService.createOrder(courseOrder.getCourseId(),courseOrder.getUserId());
            return ResponseResult.success("下单成功",payOrderRes.getPayUrl());
        }catch (Exception e){
            return ResponseResult.error("下单失败");
        }
    }

    @PostMapping("/payNotify")
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        //1.判断支付结果
        if(!request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            return "false";
        }
        //2.获取支付返回信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        //3.验签检查是否真正支付成功
        String tradeNo = params.get("out_trade_no");
        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
        //4.验签成功修改数据库对应信息
        if(!checkSignature){
            return "false";
        }
        courseOrderService.changeOrderPaySuccess(tradeNo);
        //5.返回结果
        return "true";
    }

}
