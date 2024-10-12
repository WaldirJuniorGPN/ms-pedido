package com.techchallenge4.ms_pedido.connectors.produto;

import com.techchallenge4.ms_pedido.connectors.client.produto.ClientProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_NAO_ENCOTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_SEM_ESTOQUE;

@Component
@RequiredArgsConstructor
public class ConexaoProdutoImpl implements ConexaoProduto {

    private final ClientProduto clientProduto;

    @Override
    public ProdutoResponse buscarProdutoPorId(Long id) {
        try {
            return clientProduto.buscarPorId(id).orElseThrow(() -> new PedidoExceptionHandler(
                PRODUTO_NAO_ENCOTRADO, "buscarProdutoPorId", "/produtos/" + id));
        } catch (FeignException.NotFound e) {
            throw new PedidoExceptionHandler(PRODUTO_NAO_ENCOTRADO, "buscarProdutoPorId", "/produtos/" + id);
        }
    }

    @Override
    public void atualizarQuantidadeEstoqueProduto(Long id, int quantidade) {

        try {
          clientProduto.atualizarQuantidadeEstoqueProduto(id, quantidade);
        } catch (FeignException.BadRequest e) {
            throw new PedidoExceptionHandler(PRODUTO_SEM_ESTOQUE, "atualizarQuantidadeEstoqueProduto", "/produtos/" + id);
        }
    }
}
