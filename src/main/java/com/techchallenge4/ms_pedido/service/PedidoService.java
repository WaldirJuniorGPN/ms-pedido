package com.techchallenge4.ms_pedido.service;


import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;

public interface PedidoService {

    PedidoResponse create(PedidoRequest request);

}
