package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;

import java.time.LocalDateTime;

import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.CANCELADA;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.EM_ROTA;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;

public interface PedidoResponseMock {

    static PedidoResponse getPedidoResponsePendente1() {
        return new PedidoResponse(
                1L,
                1L,
                1L, 1,
                LocalDateTime.parse("2021-08-01T00:00:00"),
                EnderecoResponseMock.getEnderecoResponse1(),
                PENDENTE);
    }

    static PedidoResponse getPedidoResponsePendente2() {
        return new PedidoResponse(
                2L,
                2L,
                2L, 2,
                LocalDateTime.parse("2021-08-02T00:00:00"),
                EnderecoResponseMock.getEnderecoResponse2(),
                PENDENTE);
    }

    static PedidoResponse getPedidoResponseEmRota() {
        return new PedidoResponse(
                1L,
                1L,
                1L, 1,
                LocalDateTime.parse("2021-08-01T00:00:00"),
                EnderecoResponseMock.getEnderecoResponse1(),
                EM_ROTA);
    }

    static PedidoResponse getPedidoResponseCancelado() {
        return new PedidoResponse(
                1L,
                1L,
                1L, 1,
                LocalDateTime.parse("2021-08-01T00:00:00"),
                EnderecoResponseMock.getEnderecoResponse1(),
                CANCELADA);
    }

}
