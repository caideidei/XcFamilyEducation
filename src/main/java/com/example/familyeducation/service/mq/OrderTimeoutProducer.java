package com.example.familyeducation.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OrderTimeoutProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.order.timeout.queue}")
    private String timeoutQueue;

    /**
     * 发送超时关单消息到队列
     *
     * @param orderId 订单ID
     * @param timeout 超时的时间戳（以毫秒为单位）
     */
    public void sendTimeoutOrderMessage(String orderId, long timeout) {
        // 构建订单超时消息
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("timeout", timeout);

        // 将消息发送到队列
        amqpTemplate.convertAndSend(timeoutQueue, message);
        log.info("已发送超时关单消息到队列，订单ID：{}，超时时间：{}", orderId, timeout);
    }
}
