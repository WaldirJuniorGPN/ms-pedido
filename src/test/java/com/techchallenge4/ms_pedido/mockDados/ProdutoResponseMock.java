package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;

public interface ProdutoResponseMock {

    static ProdutoResponse getProdutoResponse() {
        return new ProdutoResponse(
                1L,
                "Produto 1",
                "Descrição do Produto 1",
                10.0,
                10);
    }

    static ProdutoResponse getProdutoResponseQuantidade1() {
        return new ProdutoResponse(
                1L,
                "Produto 1",
                "Descrição do Produto 1",
                10.0,
                1);
    }

    static ProdutoResponse getProdutoResponseQuantidadeZero() {
        return new ProdutoResponse(
                1L,
                "Produto 1",
                "Descrição do Produto 1",
                10.0,
                0);
    }

}
