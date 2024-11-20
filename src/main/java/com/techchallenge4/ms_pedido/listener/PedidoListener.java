package com.techchallenge4.ms_pedido.listener;

import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoListener {

    private final PedidoService pedidoService;

    @RabbitListener(queues = "pedidoQueue.criar")
    public void criarPedido(PedidoRequest request) {
        log.info("stage=init method=PedidoListener.criarPedido");
        try {
            pedidoService.salvar(request);
            log.info("stage=finish method=PedidoListener.criarPedido");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
