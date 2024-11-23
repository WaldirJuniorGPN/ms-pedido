package com.techchallenge4.ms_pedido.controller.response;

import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PedidoResponse(
        Long id,
        Long clienteId,
        Long produtoId,
        Integer quantidade,
        LocalDateTime dataHoraCriacao,
        EnderecoResponse endereco,
        PedidoStatus status
) { }
