package com.techchallenge4.ms_pedido.model.dto;

import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private PedidoRequest pedidoRequest;

    private ClienteResponse clienteResponse;

    private ProdutoResponse produtoResponse;

}
