package com.techchallenge4.ms_pedido.connectors.usuario;

import com.techchallenge4.ms_pedido.connectors.client.usuario.ClientCliente;
import com.techchallenge4.ms_pedido.connectors.usuario.response.ClienteResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.CLIENTE_NAO_ENCONTRADO;

@Component
@RequiredArgsConstructor
public class ConexaoClienteImpl implements ConexaoCliente {

    private final ClientCliente clientCliente;

    @Override
    public ClienteResponse buscarClientePorId(Long clienteId) {
        try {
            return clientCliente.buscarPorId(clienteId).get();
        } catch (FeignException.NotFound e) {
            throw new PedidoExceptionHandler(CLIENTE_NAO_ENCONTRADO, "buscarClientePorId", "/clientes/" + clienteId);
        }
    }
}
