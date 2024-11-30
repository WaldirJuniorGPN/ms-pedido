package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.adapter.endereco.EnderecoAdapter;
import com.techchallenge4.ms_pedido.adapter.pedido.PedidoAdapter;
import com.techchallenge4.ms_pedido.adapter.pedidoResponse.PedidoResponseAdapter;
import com.techchallenge4.ms_pedido.connectors.cliente.ConexaoCliente;
import com.techchallenge4.ms_pedido.connectors.produto.ConexaoProduto;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.exception.PedidoErrorCode;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.Endereco;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.dto.PedidoDTO;
import com.techchallenge4.ms_pedido.repository.EnderecoRepository;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.mockDados.ClienteResponseMock.getClienteResponse;
import static com.techchallenge4.ms_pedido.mockDados.EnderecoMock.getEndereco1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente2;
import static com.techchallenge4.ms_pedido.mockDados.PedidoRequestMock.getPedidoRequestCliente2;
import static com.techchallenge4.ms_pedido.mockDados.PedidoRequestMock.getPedidoRequestQuantidade1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoRequestMock.getPedidoRequestQuantidade2;
import static com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock.getPedidoResponseCancelado;
import static com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock.getPedidoResponsePendente1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock.getPedidoResponsePendente2;
import static com.techchallenge4.ms_pedido.mockDados.ProdutoResponseMock.getProdutoResponse;
import static com.techchallenge4.ms_pedido.mockDados.ProdutoResponseMock.getProdutoResponseQuantidade1;
import static com.techchallenge4.ms_pedido.mockDados.ProdutoResponseMock.getProdutoResponseQuantidadeZero;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.CANCELADA;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Mock
    private ConexaoCliente conexaoCliente;

    @Mock
    private ConexaoProduto conexaoProduto;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private PedidoResponseAdapter pedidoResponseAdapter;

    @Mock
    private EnderecoAdapter enderecoAdapter;

    @Mock
    private PedidoAdapter pedidoAdapter;


    @Test
    void testaRecebeComSucesso() {
        var clienteId = 1L;
        var request = getPedidoRequestQuantidade1();

        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponse();

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(anyLong())).thenReturn(produtoResponse);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(PedidoDTO.class));

        pedidoService.recebe(clienteId, request);

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(PedidoDTO.class));
    }

    @Test
    void testaRecebeClienteNotFound() {

        var clienteId = 1L;
        var request = getPedidoRequestQuantidade1();

        doThrow(new PedidoExceptionHandler(CLIENTE_NAO_ENCONTRADO, "", "")).when(conexaoCliente).buscarClientePorId(request.clienteId());
        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Cliente não encontrado", resultado.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(request.clienteId());
        verify(conexaoProduto, never()).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void testaRecebeProdutoNotFound() {
        var clienteId = 1L;
        var request = getPedidoRequestQuantidade1();

        var clienteResponse = getClienteResponse();

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        doThrow(new PedidoExceptionHandler(PRODUTO_NAO_ENCONTRADO, "", "")).when(conexaoProduto).buscarProdutoPorId(request.produtoId());

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Produto não encontrado", resultado.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(request.clienteId());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void testaRecebeValidaClienteLogadoDiferentes() {
        var clienteId = 1L;
        var request = getPedidoRequestCliente2();

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Cliente logado diferente do cliente do pedido", resultado.getMessage());

        verify(conexaoCliente, never()).buscarClientePorId(anyLong());
        verify(conexaoProduto, never()).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void testaReceberErroAoValidarProdutoDisponivelEstoque() {
        var clienteId = 1L;
        var request = getPedidoRequestQuantidade2();
        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponseQuantidade1();

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(anyLong())).thenReturn(produtoResponse);
        var expected = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Produto sem estoque", expected.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void testaReceberErroAoValidarProdutoDisponivelEstoqueQuandoOEstoqueEstiverZero() {
        var clienteId = 1L;
        var request = getPedidoRequestQuantidade2();
        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponseQuantidadeZero();

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(anyLong())).thenReturn(produtoResponse);
        var expected = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Produto sem estoque", expected.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void testaSalvarComSucesso() {

        var request = getPedidoRequestQuantidade1();
        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponse();
        var pedidoDto = new PedidoDTO(request, clienteResponse, produtoResponse);
        var endereco = getEndereco1();
        var pedido = getPedidoPendente1();

        when(pedidoAdapter.buildPedido(any(PedidoDTO.class))).thenReturn(pedido);
        when(enderecoAdapter.buildEndereco(any())).thenReturn(endereco);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doNothing().when(conexaoProduto).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

        pedidoService.salvar(pedidoDto);

        verify(pedidoAdapter, times(1)).buildPedido(any(PedidoDTO.class));
        verify(enderecoAdapter, times(1)).buildEndereco(any());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(conexaoProduto, times(1)).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

    }

    @Test
    void testaSalvarErroAoSalvarEndereco() {

        var request = getPedidoRequestQuantidade1();

        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponse();
        var pedidoDto = new PedidoDTO(request, clienteResponse, produtoResponse);
        var pedido = getPedidoPendente1();
        var endereco = getEndereco1();

        when(pedidoAdapter.buildPedido(any(PedidoDTO.class))).thenReturn(pedido);
        when(enderecoAdapter.buildEndereco(any())).thenReturn(endereco);
        doThrow(PedidoExceptionHandler.class).when(enderecoRepository).save(any(Endereco.class));

        assertThrows(PedidoExceptionHandler.class, () -> pedidoService.salvar(pedidoDto));

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(conexaoProduto, never()).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

    }

    @Test
    void testaSalvarErroAoSalvarPedido() {

        var request = getPedidoRequestQuantidade1();

        var clienteResponse = getClienteResponse();
        var produtoResponse = getProdutoResponse();
        var pedidoDto = new PedidoDTO(request, clienteResponse, produtoResponse);

        var pedido = getPedidoPendente1();
        var endereco = getEndereco1();

        when(pedidoAdapter.buildPedido(any(PedidoDTO.class))).thenReturn(pedido);
        when(enderecoAdapter.buildEndereco(any())).thenReturn(endereco);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        doThrow(PedidoExceptionHandler.class).when(pedidoRepository).save(any(Pedido.class));

        assertThrows(PedidoExceptionHandler.class, () -> pedidoService.salvar(pedidoDto));

        verify(pedidoAdapter, times(1)).buildPedido(any(PedidoDTO.class));
        verify(enderecoAdapter, times(1)).buildEndereco(any());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(conexaoProduto, never()).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

    }

    @Test
    void testaSalvarErroAoAtualizarQuantidadeEstoqueProduto() {

            var request = getPedidoRequestQuantidade1();
            var clienteResponse = getClienteResponse();
            var produtoResponse = getProdutoResponse();
            var pedidoDto = new PedidoDTO(request, clienteResponse, produtoResponse);

            var pedido = getPedidoPendente1();
            var endereco = getEndereco1();

            when(pedidoAdapter.buildPedido(any(PedidoDTO.class))).thenReturn(pedido);
            when(enderecoAdapter.buildEndereco(any())).thenReturn(endereco);
            when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
            doThrow(new PedidoExceptionHandler(PedidoErrorCode.PRODUTO_SEM_ESTOQUE, "", "")).when(conexaoProduto).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

            var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.salvar(pedidoDto));

            assertEquals("Produto sem estoque", resultado.getMessage());

            verify(pedidoAdapter, times(1)).buildPedido(any(PedidoDTO.class));
            verify(enderecoAdapter, times(1)).buildEndereco(any());
            verify(enderecoRepository, times(1)).save(any(Endereco.class));
            verify(pedidoRepository, times(1)).save(any(Pedido.class));
            verify(conexaoProduto, times(1)).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());
    }





    @Test
    void testaListarPorClienteComSucesso() {

        var pedido1 = getPedidoPendente1();
        var pedido2 = getPedidoPendente2();

        var pedidoList = List.of(pedido1, pedido2);

        var pedidoResponse1 = getPedidoResponsePendente1();
        var pedidoResponse2 = getPedidoResponsePendente2();
        var pedidoResponseList = List.of(pedidoResponse1, pedidoResponse2);

        when(pedidoResponseAdapter.buildPedidoResponsePaginado(new PageImpl<>(pedidoList))).thenReturn(new PageImpl<>(pedidoResponseList));
        when(pedidoRepository.findAllByClienteId(1L, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(pedidoList));

        var resultado = pedidoService.listarPorCliente(1L, 0, 10);

        assertEquals(2, resultado.getContent().size());
        assertAll("Pedido1", () -> {
            assertEquals(1L, resultado.getContent().get(0).id());
            assertEquals(PENDENTE, resultado.getContent().get(0).status());
            assertEquals(pedido1.getDataHoraCriacao(), resultado.getContent().get(0).dataHoraCriacao());
            assertEquals(1L, resultado.getContent().get(0).clienteId());
            assertEquals(1L, resultado.getContent().get(0).produtoId());
            assertEquals(PE, resultado.getContent().get(0).endereco().uf());
            assertEquals(1, resultado.getContent().get(0).quantidade());
        });

        assertAll("Pedido2", () -> {
            assertEquals(2L, resultado.getContent().get(1).id());
            assertEquals(PENDENTE, resultado.getContent().get(1).status());
            assertEquals(pedido2.getDataHoraCriacao(), resultado.getContent().get(1).dataHoraCriacao());
            assertEquals(2L, resultado.getContent().get(1).clienteId());
            assertEquals(2L, resultado.getContent().get(1).produtoId());
            assertEquals(PE, resultado.getContent().get(1).endereco().uf());
            assertEquals(2, resultado.getContent().get(1).quantidade());
        });

        verify(pedidoResponseAdapter, times(1)).buildPedidoResponsePaginado(new PageImpl<>(pedidoList));
        verify(pedidoRepository, times(1)).findAllByClienteId(1L, PageRequest.of(0, 10));
    }

    @Test
    void testaListarPorClienteComErro() {
        doThrow(new RuntimeException()).when(pedidoRepository).findAllByClienteId(1L, PageRequest.of(0, 10));

        assertThrows(RuntimeException.class, () -> pedidoService.listarPorCliente(1L, 0, 10));

        verify(pedidoRepository, times(1)).findAllByClienteId(1L, PageRequest.of(0, 10));
    }

    @Test
    void testaCancelarPedidoComSucesso() {
        var id = 1L;
        var clienteId = 1L;
        var pedido = getPedidoPendente1();
        var pedidoSalvo = getPedidoPendente1();
        pedidoSalvo.setStatus(CANCELADA);

        when(pedidoRepository.findByIdAndClienteId(id, clienteId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedidoSalvo);
        when(pedidoResponseAdapter.buildPedidoResponse(pedido)).thenReturn(getPedidoResponseCancelado());

        var resultado = pedidoService.cancelar(id, clienteId);

        assertEquals(CANCELADA, resultado.status());

        verify(pedidoRepository, times(1)).findByIdAndClienteId(id, clienteId );
        verify(pedidoRepository, times(1)).save(pedido);
        verify(pedidoResponseAdapter, times(1)).buildPedidoResponse(pedido);
    }

    @Test
    void testaCancelarPedidoComErro() {
        var id = 1L;
        var clienteId = 1L;

        when(pedidoRepository.findByIdAndClienteId(id, clienteId)).thenReturn(empty());

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.cancelar(id, clienteId));

        assertEquals("Pedido não encontrado", resultado.getMessage());

        verify(pedidoRepository, times(1)).findByIdAndClienteId(id, clienteId);
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(pedidoResponseAdapter, never()).buildPedidoResponse(any(Pedido.class));
    }

}