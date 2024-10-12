package com.techchallenge4.ms_pedido.connectors.produto;

import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface ConexaoProduto {

    ProdutoResponse buscarProdutoPorId(@NotNull @Positive Long id);

    void atualizarQuantidadeEstoqueProduto(Long id, int quantidadeRestante);
}
