package com.techchallenge4.ms_pedido.config.amqp;

import com.techchallenge4.ms_pedido.config.Environment;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_EXCHANGE;
import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_EXCHANGE_DLQ;
import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_QUEUE_CRIAR;
import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_QUEUE_CRIAR_DLQ;

@Configuration
public class PedidoAMQPConfig {


    @Bean
    public Queue criarFilaPedidosDlq() {
        return QueueBuilder.durable(PEDIDO_QUEUE_CRIAR_DLQ)
                .build();
    }

    @Bean
    public Queue criarFilaPedidos() {
        return QueueBuilder.durable(PEDIDO_QUEUE_CRIAR)
                .deadLetterExchange(Environment.PEDIDO_EXCHANGE_DLQ)
                .build();
    }
    @Bean
    public FanoutExchange criarExchangePedidos() {
        return ExchangeBuilder.fanoutExchange(PEDIDO_EXCHANGE).build();
    }

    @Bean
    public FanoutExchange criarExchangePedidosDlq() {
        return ExchangeBuilder.fanoutExchange(PEDIDO_EXCHANGE_DLQ).build();
    }

    @Bean
    public Binding bindPedidos() {
        return BindingBuilder.bind(criarFilaPedidos()).to(criarExchangePedidos());
    }

    @Bean
    public Binding bindPedidosDql() {
        return BindingBuilder.bind(criarFilaPedidosDlq()).to(criarExchangePedidosDlq());
    }
    
    @Bean
    public RabbitAdmin criarAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

}
