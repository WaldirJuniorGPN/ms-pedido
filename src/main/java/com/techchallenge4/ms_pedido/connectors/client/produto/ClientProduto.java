package com.techchallenge4.ms_pedido.connectors.client.produto;

import com.techchallenge4.ms_pedido.config.feign.FeignConfig;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Component
@FeignClient(name = "produto", url = "${produto.url}", configuration = FeignConfig.class)
public interface ClientProduto {

    @GetMapping("/produtos/{id}")
    Optional<ProdutoResponse> buscarPorId(@PathVariable Long id);

    @PatchMapping("/produtos/{id}/quantidade/{quantidade}")
    void atualizarQuantidadeEstoqueProduto(@PathVariable("id") Long id, @PathVariable("quantidade") int quantidade);
}
