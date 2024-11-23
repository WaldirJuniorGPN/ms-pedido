package com.techchallenge4.ms_pedido.config;

public class Environment {

    private Environment() {}

    public static final String PEDIDO_QUEUE_CRIAR = "pedidoQueue.criar";
    public static final String PEDIDO_QUEUE_CRIAR_DLQ = "pedidoQueue.criar.dlq";
    public static final String PEDIDO_EXCHANGE = "pedidoExchange";
    public static final String PEDIDO_EXCHANGE_DLQ = "pedidoExchangeDlq";

}
