package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;

import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;

public interface EnderecoResponseMock {

    static EnderecoResponse getEnderecoResponse1() {
        return new EnderecoResponse(
                "123",
                "Rua 1",
                "123",
                "casa",
                "Bairro 1",
                "Cidade 1",
                "123123",
                "321321",
                PE);
    }

    static EnderecoResponse getEnderecoResponse2() {
        return new EnderecoResponse(
                "87654321",
                "Rua 2",
                "456",
                "apto",
                "Cidade 2",
                "Estado 2",
                "321321",
                "123123",
                PE);
    }

}
