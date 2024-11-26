package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;

public interface PedidoRequestMock {

    static PedidoRequest getPedidoRequestQuantidade1() {
        return new PedidoRequest(1L, 1L, 1);
    }

    static PedidoRequest getPedidoRequestQuantidade2() {
        return new PedidoRequest(1L, 1L, 2);
    }

    static PedidoRequest getPedidoRequestCliente2() {
        return new PedidoRequest(2L, 1L, 1);
    }

}
