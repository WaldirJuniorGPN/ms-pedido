package com.techchallenge4.ms_pedido.adapter.pedidoResponse;

import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.Pedido;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PedidoResponseAdapter {
    PedidoResponse buildPedidoResponse(Pedido pedido);

    Page<PedidoResponse> buildPedidoResponsePaginado(Page<Pedido> pedidosPaginados);

    List<PedidoResponse> buildPedidoResponse(List<Pedido> pedidos);
}
