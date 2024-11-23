package com.techchallenge4.ms_pedido.service;


import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.enums.EstadoEnum;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface PedidoService {

    void salvar(PedidoRequest request);

    PagedModel<PedidoResponse> listarTodos(int pagina, int tamanho);

    PedidoResponse buscarPorId(Long id);

    PedidoResponse atualizarStatus(Long id, PedidoStatus status);

    PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho);

    void recebe(Long clienteId, PedidoRequest request);

    List<PedidoResponse> listarPedidosPorStatusEUf(PedidoStatus status, EstadoEnum uf);
}
