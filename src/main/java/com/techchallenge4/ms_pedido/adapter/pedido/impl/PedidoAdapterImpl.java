package com.techchallenge4.ms_pedido.adapter.pedido.impl;

import com.techchallenge4.ms_pedido.adapter.pedido.PedidoAdapter;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.dto.PedidoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;

@Component
public class PedidoAdapterImpl implements PedidoAdapter {

    @Override
    public Pedido buildPedido(PedidoDTO pedidoDTO) {
        return Pedido.builder()
                .clienteId(pedidoDTO.getClienteResponse().id())
                .produtoId(pedidoDTO.getProdutoResponse().id())
                .quantidade(pedidoDTO.getPedidoRequest().quantidade())
                .dataHoraCriacao(LocalDateTime.now())
                .status(PENDENTE)
                .build();
    }

}
