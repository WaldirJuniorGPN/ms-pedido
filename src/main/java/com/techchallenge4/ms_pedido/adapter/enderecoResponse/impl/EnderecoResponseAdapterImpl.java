package com.techchallenge4.ms_pedido.adapter.enderecoResponse.impl;

import com.techchallenge4.ms_pedido.adapter.enderecoResponse.EnderecoResponseAdapter;
import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;
import com.techchallenge4.ms_pedido.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoResponseAdapterImpl implements EnderecoResponseAdapter {

    @Override
    public EnderecoResponse buildEnderecoResponse(Endereco endereco) {
        return new EnderecoResponse(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(),
                endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getLatitude(),
                endereco.getLongitude(), endereco.getUf());
    }

}
