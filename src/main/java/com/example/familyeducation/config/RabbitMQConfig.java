package com.example.familyeducation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//RabbitMQ配置类
@Configuration
public class RabbitMQConfig {

    /**
     * 交换机的名称
     */
    public static final String DEFAULT_EXCHANGE = "exchange-yzh";
    /**
     * 路由Key的名称
     */
    public static final String DEFAULT_ROUTE = "route-yzh";
    /**
     * 队列的名称
     */
    public static final String DEFAULT_QUEUE = "queue-yzh";

    /**
     * 声明交换机
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(DEFAULT_EXCHANGE);
    }

    /**
     * 声明队列
     * @return Queue
     */
    @Bean
    public Queue queue(){
        return new Queue(DEFAULT_QUEUE);
    }

    /**
     * 声明路由Key(交换机和队列的关系)
     * @return Binding
     */
    @Bean
    public Binding binding(DirectExchange exchange, Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(DEFAULT_ROUTE);
    }

}
