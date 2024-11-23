package com.techchallenge4.ms_pedido.connectors.client.cliente;

import com.techchallenge4.ms_pedido.config.feign.FeignConfig;
import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Component
@FeignClient(name = "cliente", url = "${cliente.url}", configuration = FeignConfig.class)
public interface ClientCliente {

    @GetMapping("/clientes/{id}")
    Optional<ClienteResponse> buscarPorId(@PathVariable("id") Long id);
}
