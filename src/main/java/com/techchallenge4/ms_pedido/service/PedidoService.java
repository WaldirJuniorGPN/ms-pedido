package com.techchallenge4.ms_pedido.service;


import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import org.springframework.data.web.PagedModel;

public interface PedidoService {

    PedidoResponse create(PedidoRequest request);

    PagedModel<PedidoResponse> listarTodos(int pagina, int tamanho);

    PedidoResponse buscarPorId(Long id);
}
