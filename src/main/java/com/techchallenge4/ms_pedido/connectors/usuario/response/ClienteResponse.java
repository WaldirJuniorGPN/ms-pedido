package com.techchallenge4.ms_pedido.connectors.usuario.response;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        EnderecoResponse endereco
) {
}
