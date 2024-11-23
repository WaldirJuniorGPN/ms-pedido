package com.techchallenge4.ms_pedido.connectors.cliente;

import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface ConexaoCliente {
    ClienteResponse buscarClientePorId(@NotNull @Positive Long clienteId);
}
