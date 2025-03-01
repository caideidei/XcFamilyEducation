package com.example.familyeducation.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.order.timeout.queue}")
    private String timeoutQueue;

    @Value("${rabbitmq.order.timeout.exchange}")
    private String timeoutExchange;

    @Bean
    public Queue timeoutQueue() {
        // 设置TTL为超时时间（单位：毫秒）
        return QueueBuilder.durable(timeoutQueue)
                .withArgument("x-message-ttl", 60000*30)  // 设置消息在队列中的存活时间为30分钟
                .withArgument("x-dead-letter-exchange", "dlx_exchange")  // 设置死信交换机
                .build();
    }

    @Bean
    public DirectExchange timeoutExchange() {
        return new DirectExchange(timeoutExchange);
    }

    @Bean
    public Binding timeoutBinding() {
        return BindingBuilder.bind(timeoutQueue()).to(timeoutExchange()).with("timeout_routing_key");
    }
}
