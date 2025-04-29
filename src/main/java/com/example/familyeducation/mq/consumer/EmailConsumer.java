package com.example.familyeducation.mq.consumer;

import com.example.familyeducation.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassDescription: mq消费者
 * @author:yzh
 * @create:2025/4/29 15:19
 **/
@Component
@RabbitListener(queues = RabbitMQConfig.DEFAULT_QUEUE)
public class EmailConsumer {

    @RabbitHandler
    public void sendEmail(Map<String,Long> map){
        Long parentId = map.get("parentId");
        Long teacherId = map.get("teacherId");
        System.out.println("教师id为："+teacherId+"想要对家长id为："+parentId+"的孩子进行试课");
    }
}
