package com.techchallenge4.ms_pedido.adapter.enderecoResponse;

import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;
import com.techchallenge4.ms_pedido.model.Endereco;

public interface EnderecoResponseAdapter {
    EnderecoResponse buildEnderecoResponse(Endereco endereco);
}
