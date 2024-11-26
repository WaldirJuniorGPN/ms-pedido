package com.techchallenge4.ms_pedido.adapter.pedidoResponse.impl;

import com.techchallenge4.ms_pedido.adapter.enderecoResponse.EnderecoResponseAdapter;
import com.techchallenge4.ms_pedido.mockDados.EnderecoResponseMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente1;
import static com.techchallenge4.ms_pedido.mockDados.PedidoMock.getPedidoPendente2;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoResponseAdapterImplTest {

    @InjectMocks
    private PedidoResponseAdapterImpl pedidoResponseAdapterImpl;

    @Mock
    private EnderecoResponseAdapter enderecoResponseAdapter;

    @Test
    void testarBuildPedidoResponse() {

        var pedido = getPedidoPendente1();

        when(enderecoResponseAdapter.buildEnderecoResponse(pedido.getEndereco())).thenReturn(
                EnderecoResponseMock.getEnderecoResponse1()
        );

        var resultado = pedidoResponseAdapterImpl.buildPedidoResponse(pedido);

        assertAll("Pedido Response", () -> {
            assertEquals(1L, resultado.id());
            assertEquals(1L, resultado.clienteId());
            assertEquals(1L, resultado.produtoId());
            assertEquals(1, resultado.quantidade());
            assertEquals(LocalDateTime.parse("2021-08-01T00:00:00"), resultado.dataHoraCriacao());
            assertEquals(PE, resultado.endereco().uf());
        });

        verify(enderecoResponseAdapter, times(1)).buildEnderecoResponse(pedido.getEndereco());

    }

    @Test
    void testarBuildPedidoResponsePaginado() {

            var pedido1 = getPedidoPendente1();
            var pedido2 = getPedidoPendente2();

            var pedidosPaginados = List.of(pedido1, pedido2);

            when(enderecoResponseAdapter.buildEnderecoResponse(pedido1.getEndereco())).thenReturn(
                EnderecoResponseMock.getEnderecoResponse1()
            );

            when(enderecoResponseAdapter.buildEnderecoResponse(pedido2.getEndereco())).thenReturn(
                EnderecoResponseMock.getEnderecoResponse2()
            );

            var resultado = pedidoResponseAdapterImpl.buildPedidoResponsePaginado(new PageImpl<>(pedidosPaginados));

            assertAll("Pedido Response 1", () -> {
                assertEquals(1L, resultado.getContent().get(0).id());
                assertEquals(1L, resultado.getContent().get(0).clienteId());
                assertEquals(1L, resultado.getContent().get(0).produtoId());
                assertEquals(1, resultado.getContent().get(0).quantidade());
                assertEquals(LocalDateTime.parse("2021-08-01T00:00:00"), resultado.getContent().get(0).dataHoraCriacao());
                assertEquals(PE, resultado.getContent().get(0).endereco().uf());
            });

        assertAll("Pedido Response 1", () -> {
            assertEquals(2L, resultado.getContent().get(1).id());
            assertEquals(2L, resultado.getContent().get(1).clienteId());
            assertEquals(2L, resultado.getContent().get(1).produtoId());
            assertEquals(2, resultado.getContent().get(1).quantidade());
            assertEquals(LocalDateTime.parse("2021-08-02T00:00:00"), resultado.getContent().get(1).dataHoraCriacao());
            assertEquals(PE, resultado.getContent().get(1).endereco().uf());
        });

        verify(enderecoResponseAdapter, times(1)).buildEnderecoResponse(pedido1.getEndereco());
        verify(enderecoResponseAdapter, times(1)).buildEnderecoResponse(pedido2.getEndereco());

    }

    @Test
    void testaBuidPedidoResponse() {
        var pedido1 = getPedidoPendente1();
        var pedido2 = getPedidoPendente2();

        var pedidoList = List.of(pedido1, pedido2);

        when(enderecoResponseAdapter.buildEnderecoResponse(pedido1.getEndereco())).thenReturn(EnderecoResponseMock.getEnderecoResponse1());
        when(enderecoResponseAdapter.buildEnderecoResponse(pedido2.getEndereco())).thenReturn(EnderecoResponseMock.getEnderecoResponse2());

        var resultado = pedidoResponseAdapterImpl.buildPedidoResponse(pedidoList);
        assertAll("Pedido Response 1", () -> {
            assertEquals(1L, resultado.get(0).id());
            assertEquals(1L, resultado.get(0).clienteId());
            assertEquals(1L, resultado.get(0).produtoId());
            assertEquals(1, resultado.get(0).quantidade());
            assertEquals(LocalDateTime.parse("2021-08-01T00:00:00"), resultado.get(0).dataHoraCriacao());
            assertEquals(PE, resultado.get(0).endereco().uf());
        });

        assertAll("Pedido Response 2", () -> {
            assertEquals(2L, resultado.get(1).id());
            assertEquals(2L, resultado.get(1).clienteId());
            assertEquals(2L, resultado.get(1).produtoId());
            assertEquals(2, resultado.get(1).quantidade());
            assertEquals(LocalDateTime.parse("2021-08-02T00:00:00"), resultado.get(1).dataHoraCriacao());
            assertEquals(PE, resultado.get(1).endereco().uf());
        });

        verify(enderecoResponseAdapter, times(1)).buildEnderecoResponse(pedido1.getEndereco());
        verify(enderecoResponseAdapter, times(1)).buildEnderecoResponse(pedido2.getEndereco());

    }

}