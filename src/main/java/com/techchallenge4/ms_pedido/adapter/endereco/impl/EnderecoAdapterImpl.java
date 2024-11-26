package com.techchallenge4.ms_pedido.adapter.endereco.impl;

import com.techchallenge4.ms_pedido.adapter.endereco.EnderecoAdapter;
import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import com.techchallenge4.ms_pedido.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoAdapterImpl implements EnderecoAdapter {

    @Override
    public Endereco buildEndereco(ClienteResponse clienteResponse) {
        return Endereco.builder()
                .cep(clienteResponse.endereco().cep())
                .uf(clienteResponse.endereco().uf())
                .cidade(clienteResponse.endereco().cidade())
                .bairro(clienteResponse.endereco().bairro())
                .complemento(clienteResponse.endereco().complemento())
                .logradouro(clienteResponse.endereco().logradouro())
                .numero(clienteResponse.endereco().numero())
                .latitude(clienteResponse.endereco().latitude())
                .longitude(clienteResponse.endereco().longitude())
                .build();
    }

}
