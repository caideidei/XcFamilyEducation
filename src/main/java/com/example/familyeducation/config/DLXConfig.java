package com.example.familyeducation.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DLXConfig {

    @Value("${rabbitmq.order.timeout.dlx.queue}")
    private String dlxQueue;

    @Value("${rabbitmq.order.timeout.dlx.exchange}")
    private String dlxExchange;

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(dlxQueue).build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("dlx_routing_key");
    }
}
