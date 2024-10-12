package com.techchallenge4.ms_pedido.connectors.produto.response;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        Double preco,
        Integer quantidadeEstoque
) {
}
