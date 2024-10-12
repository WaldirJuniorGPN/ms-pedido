package com.techchallenge4.ms_pedido.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoRequest(

        @NotNull
        @Positive
        Long usuarioId,

        @NotNull
        @Positive
        Long produtoId,

        @NotNull
        Integer quantidade
) {}
