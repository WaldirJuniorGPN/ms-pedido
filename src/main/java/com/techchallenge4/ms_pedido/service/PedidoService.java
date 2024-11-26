package com.techchallenge4.ms_pedido.service;


import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.model.dto.PedidoDTO;
import com.techchallenge4.ms_pedido.model.enums.UfEnum;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface PedidoService {

    void salvar(PedidoDTO pedidoDTO);

    PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho);

    void recebe(Long clienteId, PedidoRequest request);
}
