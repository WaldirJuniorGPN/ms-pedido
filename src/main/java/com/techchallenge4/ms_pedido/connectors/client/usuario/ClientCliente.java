package com.techchallenge4.ms_pedido.connectors.client.usuario;

import com.techchallenge4.ms_pedido.connectors.usuario.response.ClienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Component
@FeignClient(name = "usuario", url = "http://localhost:3002")
public interface ClientCliente {

    @GetMapping("/clientes/{id}")
    Optional<ClienteResponse> buscarPorId(@PathVariable("id") Long id);
}
