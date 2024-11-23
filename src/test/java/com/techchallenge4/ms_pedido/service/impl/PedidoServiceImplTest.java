package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.connectors.produto.ConexaoProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.connectors.cliente.ConexaoCliente;
import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.Endereco;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.repository.EnderecoRepository;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.model.enums.EstadoEnum.AC;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;
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
import static org.mockito.MockitoAnnotations.openMocks;

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

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void recebe() {
        var clienteId = 1L;
        var request = new PedidoRequest(1L, 1L, 1);
        var enderecoResponse = new EnderecoResponse("123", "Rua 1", "123", "Bairro 1",
                "Cidade 1", "Estado 1", "123123", "321321", AC);
        var clienteResponse = new ClienteResponse(1L, "Cliente 1", "email", "123123", enderecoResponse);
        var produtoResponse = new ProdutoResponse(1L, "Produto 1", "Descrição do Produto 1", 10.0,
                10);

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(anyLong())).thenReturn(produtoResponse);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(PedidoRequest.class));

        pedidoService.recebe(clienteId, request);

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void recebeClienteNotFound() {

        var clienteId = 1L;
        var request = new PedidoRequest(1L, 1L, 1);

        doThrow(new PedidoExceptionHandler(CLIENTE_NAO_ENCONTRADO, "", "")).when(conexaoCliente).buscarClientePorId(request.clienteId());
        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Cliente não encontrado", resultado.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(request.clienteId());
        verify(conexaoProduto, never()).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void recebeProdutoNotFound() {
        var clienteId = 1L;
        var request = new PedidoRequest(1L, 1L, 1);
        var enderecoResponse = new EnderecoResponse("123", "Rua 1", "123", "Bairro 1",
                "Cidade 1", "Estado 1", "123123", "321321", AC);
        var clienteResponse = new ClienteResponse(1L, "Cliente 1", "email", "123123", enderecoResponse);

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        doThrow(new PedidoExceptionHandler(PRODUTO_NAO_ENCONTRADO, "", "")).when(conexaoProduto).buscarProdutoPorId(request.produtoId());

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Produto não encontrado", resultado.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(request.clienteId());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void receberValidarProdutoDisponivelEstoque() {
        var clienteId = 1L;
        var request = new PedidoRequest(1L, 1L, 2);
        var enderecoResponse = new EnderecoResponse("123", "Rua 1", "123", "Bairro 1",
                "Cidade 1", "Estado 1", "123123", "321321", AC);
        var clienteResponse = new ClienteResponse(1L, "Cliente 1", "email", "123123", enderecoResponse);
        var produtoResponse = new ProdutoResponse(1L, "Produto 1", "Descrição do Produto 1", 10.0,
                1);

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(anyLong())).thenReturn(produtoResponse);
        var expected = assertThrows(PedidoExceptionHandler.class, () -> pedidoService.recebe(clienteId, request));

        assertEquals("Produto sem estoque", expected.getMessage());

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoRequest.class));
    }

    @Test
    void salvar() {

        var request = new PedidoRequest(1L, 1L, 1);
        var enderecoResponse = new EnderecoResponse("123", "Rua 1", "123", "Bairro 1",
                "Cidade 1", "Estado 1", "123123", "321321", AC);
        var clienteResponse = new ClienteResponse(1L, "Cliente 1", "email", "123123", enderecoResponse);
        var produtoResponse = new ProdutoResponse(1L, "Produto 1", "Descrição do Produto 1", 10.0,
                10);

        var endereco = new Endereco(123L, "Rua 1", "123", "casa", "Bairro 1",
                "Cidade 1", AC, "123123", "321321", "123123", null);

        var pedido = new Pedido(123L, 1L, PENDENTE, 1L, 1,
                LocalDateTime.parse("2021-08-01T00:00:00"), endereco);

        when(conexaoCliente.buscarClientePorId(request.clienteId())).thenReturn(clienteResponse);
        when(conexaoProduto.buscarProdutoPorId(request.produtoId())).thenReturn(produtoResponse);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doNothing().when(conexaoProduto).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

        pedidoService.salvar(request);

        verify(conexaoCliente, times(1)).buscarClientePorId(anyLong());
        verify(conexaoProduto, times(1)).buscarProdutoPorId(anyLong());
        verify(enderecoRepository, times(1)).save(any());
        verify(pedidoRepository, times(1)).save(any());
        verify(conexaoProduto, times(1)).atualizarQuantidadeEstoqueProduto(anyLong(), anyInt());

    }

//    @Test
    void listarTodos() {
    }

//    @Test
    void listarPedidosPorStatusEUf() {
    }

//    @Test
    void buscarPorId() {
    }

//    @Test
    void atualizarStatus() {
    }

//    @Test
    void listarPorCliente() {
    }
}