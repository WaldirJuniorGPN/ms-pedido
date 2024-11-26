package com.techchallenge4.ms_pedido.service;

import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import com.techchallenge4.ms_pedido.model.enums.UfEnum;

import java.util.List;

public interface PedidoServiceInterno {

    List<PedidoResponse> listarPedidosPorStatusEUf(PedidoStatus status, UfEnum uf);

    PedidoResponse buscarPorId(Long id);

    PedidoResponse atualizarStatus(Long id, PedidoStatus status);

}
