package com.pedidos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue pedidosQueue() {
        return QueueBuilder.durable("pedidos.entrada.clayton")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "pedidos.entrada.clayton.dlq")
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue("pedidos.entrada.clayton.dlq");
    }

    @Bean
    public Queue sucessoQueue() {
        return new Queue("pedidos.status.sucesso.clayton");
    }

    @Bean
    public Queue falhaQueue() {
        return new Queue("pedidos.status.falha.clayton");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}