package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.model.Pedido;

import java.time.LocalDateTime;

import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;

public interface PedidoMock {

    static Pedido getPedidoPendente1() {
        return Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .status(PENDENTE)
                .produtoId(1L)
                .quantidade(1)
                .dataHoraCriacao(LocalDateTime.parse("2021-08-01T00:00:00"))
                .endereco(EnderecoMock.getEndereco1())
                .build();
    }

    static Pedido getPedidoPendente2() {
        return Pedido.builder()
                .id(2L)
                .clienteId(2L)
                .status(PENDENTE)
                .produtoId(2L)
                .quantidade(2)
                .dataHoraCriacao(LocalDateTime.parse("2021-08-02T00:00:00"))
                .endereco(EnderecoMock.getEndereco2())
                .build();
    }

}
