package com.techchallenge4.ms_pedido.connectors.cliente.response;

import com.techchallenge4.ms_pedido.model.enums.UfEnum;

public record EnderecoResponse(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String latitude,
        String longitude,
        UfEnum uf
) {
}
