package com.example.familyeducation.service;

import com.alipay.api.AlipayApiException;
import com.example.familyeducation.dto.PayOrderRes;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2025/2/28 20:39
 **/
public interface CourseOrderService {
    PayOrderRes createOrder(String courseId, String userId) throws AlipayApiException;

    void changeOrderPaySuccess(String orderId);

    void changeOrderClose(String orderId);
}
