package com.amazon.apporderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange mainTopicExchange() {
        return new TopicExchange("mainExchangeForB29");
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue("amazon-notification");
    }

    @Bean
    public Queue shippingQueue() {
        return new Queue("amazon-shipping");
    }

    @Bean
    public Binding notificationBind() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(mainTopicExchange())
                .with("for-only-notification");
    }

    @Bean
    public Binding shippingBind() {
        return BindingBuilder
                .bind(shippingQueue())
                .to(mainTopicExchange())
                .with("for-only-shipping");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
