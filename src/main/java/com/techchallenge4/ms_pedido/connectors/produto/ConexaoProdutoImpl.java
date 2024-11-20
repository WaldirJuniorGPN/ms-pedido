package com.techchallenge4.ms_pedido.connectors.produto;

import com.techchallenge4.ms_pedido.connectors.client.produto.ClientProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_SEM_ESTOQUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConexaoProdutoImpl implements ConexaoProduto {

    private static final String ROOT_PATH = "/produtos/";

    private final ClientProduto clientProduto;

    @Override
    public ProdutoResponse buscarProdutoPorId(Long id) {
        try {
            return clientProduto.buscarPorId(id).orElseThrow(() -> new PedidoExceptionHandler(
                    PRODUTO_NAO_ENCONTRADO, "buscarProdutoPorId", ROOT_PATH + id));
        } catch (FeignException.NotFound e) {
            log.warn("Produto não encontrado. Message = {}", e.getMessage());
            throw new PedidoExceptionHandler(PRODUTO_NAO_ENCONTRADO, "buscarProdutoPorId", ROOT_PATH + id);
        }
    }

    @Override
    public void atualizarQuantidadeEstoqueProduto(Long id, int quantidade) {

        try {
          clientProduto.atualizarQuantidadeEstoqueProduto(id, quantidade);
        } catch (FeignException.BadRequest e) {
            log.error("Erro ao atualizar estoque. Message = {}", e.getMessage());
            throw new PedidoExceptionHandler(PRODUTO_SEM_ESTOQUE, "atualizarQuantidadeEstoqueProduto", ROOT_PATH + id);
        }
    }
}
