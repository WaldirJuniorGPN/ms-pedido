package com.techchallenge4.ms_pedido.config.amqp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PedidoAMQPConfigTest {

    @InjectMocks
    private PedidoAMQPConfig pedidoAMQPConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private RabbitAdmin rabbitAdmin;

    @Test
    void testaCriarFilaPedidosDlqReturnsDurableQueue() {
        Queue queue = pedidoAMQPConfig.criarFilaPedidosDlq();
        assertNotNull(queue);
        assertTrue(queue.isDurable());
        assertEquals("pedidoQueue.criar.dlq", queue.getName());
    }

    @Test
    void testaCriarFilaPedidosReturnsDurableQueue() {
        Queue queue = pedidoAMQPConfig.criarFilaPedidos();
        assertNotNull(queue);
        assertTrue(queue.isDurable());
        assertEquals("pedidoQueue.criar", queue.getName());
        assertEquals("pedidoExchangeDlq", queue.getArguments().get("x-dead-letter-exchange"));
    }

    @Test
    void testaCriarExchangePedidosReturnsFanoutExchange() {
        var fanoutExchange = pedidoAMQPConfig.criarExchangePedidos();
        assertNotNull(fanoutExchange);
        assertEquals("pedidoExchange", fanoutExchange.getName());
    }

    @Test
    void testaCriarExchangePedidosDlqReturnsFanoutExchange() {
        var fanoutExchange = pedidoAMQPConfig.criarExchangePedidosDlq();
        assertNotNull(fanoutExchange);
        assertEquals("pedidoExchangeDlq", fanoutExchange.getName());
    }

    @Test
    void testaBindPedidosReturnsBinding() {
        var binding = pedidoAMQPConfig.bindPedidos();
        assertNotNull(binding);
        assertEquals("pedidoQueue.criar", binding.getDestination());
        assertEquals("pedidoExchange", binding.getExchange());
    }

    @Test
    void testaBindPedidosDqlReturnsBinding() {
        var binding = pedidoAMQPConfig.bindPedidosDql();
        assertNotNull(binding);
        assertEquals("pedidoQueue.criar.dlq", binding.getDestination());
        assertEquals("pedidoExchangeDlq", binding.getExchange());
    }

    @Test
    void testaCriarAdminReturnsRabbitAdmin() {
        var admin = pedidoAMQPConfig.criarAdmin(connectionFactory);
        assertNotNull(admin);
    }

    @Test
    void testaInicializaAdminReturnsApplicationReadyEvent() {

        var listener = pedidoAMQPConfig.inicializaAdmin(rabbitAdmin);
        var event = mock(ApplicationReadyEvent.class);

        listener.onApplicationEvent(event);

        verify(rabbitAdmin, times(1)).initialize();
    }

    @Test
    void testaMessageConverterReturnsJackson2JsonMessageConverter() {
        var messageConverter = pedidoAMQPConfig.messageConverter();
        assertNotNull(messageConverter);
    }

    @Test
    void testaRabbitTemplateReturnsRabbitTemplate() {
        var rabbitTemplate = pedidoAMQPConfig.rabbitTemplate(connectionFactory, pedidoAMQPConfig.messageConverter());
        assertNotNull(rabbitTemplate);
        assertInstanceOf(Jackson2JsonMessageConverter.class, rabbitTemplate.getMessageConverter());
    }

}