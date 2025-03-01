package com.example.familyeducation.service.mq;

import com.example.familyeducation.service.CourseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OrderTimeoutConsumer {

    @Autowired
    private CourseOrderService courseOrderService;

    @RabbitListener(queues = "${rabbitmq.order.timeout.queue}")
    public void receiveTimeoutOrderMessage(Map<String, Object> message) {
        String orderId = (String) message.get("orderId");
        long timeout = (long) message.get("timeout");

        log.info("接收到超时关单消息，订单ID：{}，超时时间：{}", orderId, timeout);

        // 获取当前时间，如果当前时间大于超时时间，则关单
        if (System.currentTimeMillis() > timeout) {
            log.info("订单超时，正在进行关单操作，订单ID：{}", orderId);
            courseOrderService.changeOrderClose(orderId);
        }
    }
}
