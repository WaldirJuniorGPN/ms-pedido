package com.techchallenge4.ms_pedido.adapter.pedidoResponse.impl;

import com.techchallenge4.ms_pedido.adapter.enderecoResponse.EnderecoResponseAdapter;
import com.techchallenge4.ms_pedido.adapter.pedidoResponse.PedidoResponseAdapter;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PedidoResponseAdapterImpl implements PedidoResponseAdapter {

    private final EnderecoResponseAdapter enderecoResponseAdapter;

    @Override
    public PedidoResponse buildPedidoResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .produtoId(pedido.getProdutoId())
                .quantidade(pedido.getQuantidade())
                .dataHoraCriacao(pedido.getDataHoraCriacao())
                .endereco(enderecoResponseAdapter.buildEnderecoResponse(pedido.getEndereco()))
                .status(pedido.getStatus())
                .build();
    }

    @Override
    public Page<PedidoResponse> buildPedidoResponsePaginado(Page<Pedido> pedidosPaginados) {
        return pedidosPaginados.map(this::buildPedidoResponse);
    }

    @Override
    public List<PedidoResponse> buildPedidoResponse(List<Pedido> pedidos) {
        return pedidos.stream().map(this::buildPedidoResponse).toList();
    }

}
