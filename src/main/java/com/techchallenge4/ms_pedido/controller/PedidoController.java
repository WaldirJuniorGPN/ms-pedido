package com.techchallenge4.ms_pedido.controller;

import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criar (@Valid @RequestBody PedidoRequest request) {

        var pedidosResponse = pedidoService.create(request);

        return ResponseEntity.ok(pedidosResponse);
    }

    @GetMapping
    public ResponseEntity<PagedModel<PedidoResponse>> listarTodos(@RequestParam(defaultValue = "0") int pagina,
                                                                  @RequestParam(defaultValue = "10") int tamanho) {

        var pedidosResponse = pedidoService.listarTodos(pagina, tamanho);

        return ResponseEntity.ok(pedidosResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {

        var pedidoResponse = pedidoService.buscarPorId(id);

        return ResponseEntity.ok(pedidoResponse);
    }

}
