package com.techchallenge4.ms_pedido.connectors.usuario;

import com.techchallenge4.ms_pedido.connectors.usuario.response.ClienteResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface ConexaoCliente {
    ClienteResponse buscarClientePorId(@NotNull @Positive Long usuarioId);
}
