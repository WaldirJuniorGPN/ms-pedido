package com.techchallenge4.ms_pedido.service;


import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import org.springframework.data.web.PagedModel;

public interface PedidoService {

    PedidoResponse create(PedidoRequest request);

    PagedModel<PedidoResponse> listarTodos(int pagina, int tamanho);

    PedidoResponse buscarPorId(Long id);

    PedidoResponse atualizarStatus(Long id, PedidoStatus status);

    PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho);
}
