package com.example.familyeducation.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassDescription: 支付返回信息
 * @Author:小菜
 * @Create:2025/2/28 20:41
 **/
@Data
@Builder
public class PayOrderRes {
    private String orderId;
    private String payUrl;
}
