package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.adapter.pedidoResponse.PedidoResponseAdapter;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock;
import com.techchallenge4.ms_pedido.model.Endereco;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente2;
import static com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock.getPedidoResponsePendente1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoResponseMock.getPedidoResponsePendente2;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.EM_ROTA;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceInternoImplTest {

    @InjectMocks
    private PedidoServiceInternoImpl pedidoServiceInternoImpl;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoResponseAdapter pedidoResponseAdapter;

    @Test
    void testaListarPedidosPorStatusEUfComSucesso() {

        var pedido1 = getPedidoPendente1();
        var pedido2 = getPedidoPendente2();
        var pedidoList = List.of(pedido1, pedido2);

        var pedidoResponse1 = getPedidoResponsePendente1();
        var pedidoResponse2 = getPedidoResponsePendente2();
        var pedidoResponseList = List.of(pedidoResponse1, pedidoResponse2);

        when(pedidoResponseAdapter.buildPedidoResponse(pedidoList)).thenReturn(pedidoResponseList);
        when(pedidoRepository.findAllByStatusAndEndereco_Uf(PENDENTE, PE)).thenReturn(pedidoList);

        var resultado = pedidoServiceInternoImpl.listarPedidosPorStatusEUf(PENDENTE, PE);

        assertEquals(2, resultado.size());
        assertAll("Pedido1", () -> {
            assertEquals(1L, resultado.get(0).id());
            assertEquals(PENDENTE, resultado.get(0).status());
            assertEquals(pedido1.getDataHoraCriacao(), resultado.get(0).dataHoraCriacao());
            assertEquals(1L, resultado.get(0).clienteId());
            assertEquals(1L, resultado.get(0).produtoId());
            assertEquals(PE, resultado.get(0).endereco().uf());
            assertEquals(1, resultado.get(0).quantidade());
        });

        assertAll("Pedido2", () -> {
            assertEquals(2L, resultado.get(1).id());
            assertEquals(PENDENTE, resultado.get(1).status());
            assertEquals(pedido2.getDataHoraCriacao(), resultado.get(1).dataHoraCriacao());
            assertEquals(2L, resultado.get(1).clienteId());
            assertEquals(2L, resultado.get(1).produtoId());
            assertEquals(PE, resultado.get(1).endereco().uf());
            assertEquals(2, resultado.get(1).quantidade());
        });

        verify(pedidoResponseAdapter, times(1)).buildPedidoResponse(pedidoList);
        verify(pedidoRepository, times(1)).findAllByStatusAndEndereco_Uf(PENDENTE, PE);

    }

    @Test
    void testaListarPedidosPorStatusEUfSemPedidos() {

        when(pedidoRepository.findAllByStatusAndEndereco_Uf(PENDENTE, PE)).thenReturn(List.of());

        var resultado = pedidoServiceInternoImpl.listarPedidosPorStatusEUf(PENDENTE, PE);

        assertEquals(0, resultado.size());

        verify(pedidoRepository, times(1)).findAllByStatusAndEndereco_Uf(PENDENTE, PE);

    }

    @Test
    void testaListarPedidosPorStatusEUfComErro() {

        doThrow(new RuntimeException()).when(pedidoRepository).findAllByStatusAndEndereco_Uf(PENDENTE, PE);

        assertThrows(RuntimeException.class, () -> pedidoServiceInternoImpl.listarPedidosPorStatusEUf(PENDENTE, PE));

        verify(pedidoRepository, times(1)).findAllByStatusAndEndereco_Uf(PENDENTE, PE);

    }

    @Test
    void testaBuscarPorId() {

        var pedido = getPedidoPendente1();

        when(pedidoResponseAdapter.buildPedidoResponse(pedido)).thenReturn(getPedidoResponsePendente1());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        var resultado = pedidoServiceInternoImpl.buscarPorId(1L);

        assertEquals(1L, resultado.id());
        assertEquals(PENDENTE, resultado.status());
        assertEquals(pedido.getDataHoraCriacao(), resultado.dataHoraCriacao());
        assertEquals(1L, resultado.clienteId());
        assertEquals(1L, resultado.produtoId());
        assertEquals(PE, resultado.endereco().uf());
        assertEquals(1, resultado.quantidade());

        verify(pedidoResponseAdapter, times(1)).buildPedidoResponse(pedido);
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testaBuscarPorIdPedidoNaoEncontrado() {

        when(pedidoRepository.findById(1L)).thenReturn(empty());

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoServiceInternoImpl.buscarPorId(1L));

        assertEquals("Pedido não encontrado", resultado.getMessage());

        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testaAtualizarStatus() {

        var pedido = getPedidoPendente1();
        var pedidoResponse = PedidoResponseMock.getPedidoResponseEmRota();

        when(pedidoResponseAdapter.buildPedidoResponse(pedido)).thenReturn(pedidoResponse);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        var resultado = pedidoServiceInternoImpl.atualizarStatus(1L, EM_ROTA);

        assertEquals(1L, resultado.id());
        assertEquals(EM_ROTA, resultado.status());
        assertEquals(pedido.getDataHoraCriacao(), resultado.dataHoraCriacao());
        assertEquals(1L, resultado.clienteId());
        assertEquals(1L, resultado.produtoId());
        assertEquals(PE, resultado.endereco().uf());
        assertEquals(1, resultado.quantidade());

        verify(pedidoResponseAdapter, times(1)).buildPedidoResponse(pedido);
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testaAtualizarStatusPedidoNaoEncontrado() {

        when(pedidoRepository.findById(1L)).thenReturn(empty());

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> pedidoServiceInternoImpl.atualizarStatus(1L, EM_ROTA));

        assertEquals("Pedido não encontrado", resultado.getMessage());

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testaAtualizarStatusPedidoComErroAoSalvar() {

        var endereco = new Endereco(1L, "Rua 1", "123", "casa", "Bairro 1",
                "Cidade 1", PE, "123123", "321321", "123123", null);

        var pedido = new Pedido(1L, 1L, PENDENTE, 1L, 1,
                LocalDateTime.parse("2021-08-01T00:00:00"), endereco);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        doThrow(new RuntimeException()).when(pedidoRepository).save(pedido);

        assertThrows(RuntimeException.class, () -> pedidoServiceInternoImpl.atualizarStatus(1L, EM_ROTA));

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(pedido);
    }

}