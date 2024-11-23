package com.techchallenge4.ms_pedido.connectors.cliente.response;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        EnderecoResponse endereco
) {
}
