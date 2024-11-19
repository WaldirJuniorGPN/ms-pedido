package com.techchallenge4.ms_pedido.config.listener;

import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PedidoListener {

    private final PedidoService pedidoService;

    @Bean
    public Consumer<PedidoRequest> pedidoConsumer() {
        try {
            log.info("Pedido recebido:");
            return pedidoService::salvar;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
