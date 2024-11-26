package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;

import static com.techchallenge4.ms_pedido.mockDados.EnderecoResponseMock.getEnderecoResponse1;

public interface ClienteResponseMock {

    static ClienteResponse getClienteResponse() {
        return new ClienteResponse(
                1L,
                "Cliente 1",
                "email",
                "123123",
                getEnderecoResponse1());
    }

}
