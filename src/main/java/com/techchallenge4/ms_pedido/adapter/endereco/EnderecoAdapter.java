package com.techchallenge4.ms_pedido.adapter.endereco;

import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import com.techchallenge4.ms_pedido.model.Endereco;

public interface EnderecoAdapter {

    Endereco buildEndereco(ClienteResponse clienteResponse);

}
