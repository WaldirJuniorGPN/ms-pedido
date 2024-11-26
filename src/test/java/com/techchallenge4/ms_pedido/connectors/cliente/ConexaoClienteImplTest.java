package com.techchallenge4.ms_pedido.connectors.cliente;

import com.techchallenge4.ms_pedido.connectors.client.cliente.ClientCliente;
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

import static com.techchallenge4.ms_pedido.mockDados.ClienteResponseMock.getClienteResponse;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConexaoClienteImplTest {

    @InjectMocks
    private ConexaoClienteImpl conexaoClienteImpl;

    @Mock
    private ClientCliente clientCliente;

    @Test
    void testaBuscarClientePorIdSucesso() {
        var clienteId = 1L;

        when(clientCliente.buscarPorId(clienteId)).thenReturn(Optional.of(getClienteResponse()));

        var resultado = conexaoClienteImpl.buscarClientePorId(clienteId);

        assertAll("Cliente Response", () -> {
            assertEquals(1L, resultado.id());
            assertEquals("Cliente 1", resultado.nome());
            assertEquals("email", resultado.email());
            assertEquals(PE, resultado.endereco().uf());
            assertEquals("123123", resultado.telefone());
        });

        verify(clientCliente, times(1)).buscarPorId(clienteId);
    }

    @Test
    void testaBuscarClientePorIdClienteNaoEncontrado() {
        var clienteId = 1L;
        var request = Request.create(Request.HttpMethod.GET, "/clientes/" + clienteId, Map.of(), null, Util.UTF_8);

        when(clientCliente.buscarPorId(clienteId)).thenThrow(new FeignException.NotFound("Cliente não encontrado", request, null, null));

        var resultado = assertThrows(PedidoExceptionHandler.class, () -> conexaoClienteImpl.buscarClientePorId(clienteId));

        assertEquals("Cliente não encontrado", resultado.getMessage());

        verify(clientCliente, times(1)).buscarPorId(clienteId);
    }

}