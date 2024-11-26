package com.techchallenge4.ms_pedido.adapter.pedido;

import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.dto.PedidoDTO;

public interface PedidoAdapter {
    Pedido buildPedido(PedidoDTO pedidoDTO);
}
