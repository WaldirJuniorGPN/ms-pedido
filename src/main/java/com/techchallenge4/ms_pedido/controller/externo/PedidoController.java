package com.techchallenge4.ms_pedido.controller.externo;

import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private static final String HEADER_CLIENT_ID = "X-ID-USER";

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void recebe (@NotNull @Positive @RequestHeader(HEADER_CLIENT_ID) Long clientId, @Valid @RequestBody PedidoRequest request) {

        pedidoService.recebe(clientId, request);

    }

    @GetMapping("/cliente")
    public ResponseEntity<PagedModel<PedidoResponse>> listarPorCliente(@NotNull @Positive @RequestHeader(HEADER_CLIENT_ID) Long clienteId,
                                                                       @RequestParam(defaultValue = "0") int pagina,
                                                                       @RequestParam(defaultValue = "10") int tamanho) {

        var pedidosResponse = pedidoService.listarPorCliente(clienteId, pagina, tamanho);

        return ResponseEntity.ok(pedidosResponse);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id,
                                                   @NotNull @Positive @RequestHeader(HEADER_CLIENT_ID) Long clientId) {
        var pedidoResponse = pedidoService.cancelar(id, clientId);

        return ResponseEntity.ok(pedidoResponse);
    }

}
