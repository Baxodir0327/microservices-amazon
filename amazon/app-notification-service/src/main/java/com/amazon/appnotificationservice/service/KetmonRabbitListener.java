package com.amazon.appnotificationservice.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class KetmonRabbitListener {

    @RabbitListener(queues = {"amazon-notification"})
    public void eshitaylik(Message message){
        System.out.println(message);
    }

    @RabbitListener(queues = {"amazon-shipping"})
    public void eshitaylik2(Message message){
        System.out.println("--------------");
        System.out.println(message);
    }

}
