package com.techchallenge4.ms_pedido.connectors.produto;

import com.techchallenge4.ms_pedido.connectors.client.produto.ClientProduto;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import feign.FeignException;
import feign.Request;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.techchallenge4.ms_pedido.mockDados.ProdutoResponseMock.getProdutoResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConexaoProdutoImplTest {

    @InjectMocks
    private ConexaoProdutoImpl conexaoProdutoImpl;

    @Mock
    private ClientProduto clientProduto;

    @Test
    void testaBuscarProdutoPorIdSucesso() {
        var id = 1L;

        when(clientProduto.buscarPorId(id)).thenReturn(Optional.of(getProdutoResponse()));

        var resulado = conexaoProdutoImpl.buscarProdutoPorId(id);

        assertAll("Produto Response", () ->{
            assertEquals(1L, resulado.id());
            assertEquals("Produto 1", resulado.nome());
            assertEquals("Descrição do Produto 1", resulado.descricao());
            assertEquals(10.0, resulado.preco());
            assertEquals(10, resulado.quantidadeEstoque());
        });

        verify(clientProduto, times(1)).buscarPorId(id);
    }

    @Test
    void testaBuscarProdutoPorIdErroProdutoNaoEncontrado() {
        var id = 1L;
        var request = Request.create(Request.HttpMethod.GET, "/produtos/" + id, Map.of(), null, Util.UTF_8);

        when(clientProduto.buscarPorId(id)).thenThrow(new FeignException.NotFound("Produto não encontrado", request, null, null));

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> conexaoProdutoImpl.buscarProdutoPorId(id));

        assertEquals("Produto não encontrado", resultado.getMessage());

        verify(clientProduto, times(1)).buscarPorId(id);
    }

    @Test
    void testaAtualizarQuantidadeEstoqueProdutoSucesso() {
        var id = 1L;
        var quantidade = 1;

        doNothing().when(clientProduto).atualizarQuantidadeEstoqueProduto(id, quantidade);
        conexaoProdutoImpl.atualizarQuantidadeEstoqueProduto(id, quantidade);

        verify(clientProduto, times(1)).atualizarQuantidadeEstoqueProduto(id, quantidade);
    }

    @Test
    void testaAtualizarQuantidadeEstoqueProdutoSemEstoque() {
        var id = 1L;
        var quantidade = 1;

        var request = Request.create(Request.HttpMethod.PATCH, "/produtos/atualizar/estoque/" + id + "/" + quantidade, Map.of(),
                null, Util.UTF_8);

        doThrow(new FeignException.BadRequest("Produto sem estoque", request, null, null))
                .when(clientProduto).atualizarQuantidadeEstoqueProduto(id, quantidade);

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> conexaoProdutoImpl.atualizarQuantidadeEstoqueProduto(id, quantidade));

        assertEquals("Produto sem estoque", resultado.getMessage());

        verify(clientProduto, times(1)).atualizarQuantidadeEstoqueProduto(id, quantidade);
    }

}