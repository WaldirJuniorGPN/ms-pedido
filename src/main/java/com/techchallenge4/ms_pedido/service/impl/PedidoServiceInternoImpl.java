package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.adapter.pedidoResponse.PedidoResponseAdapter;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import com.techchallenge4.ms_pedido.model.enums.UfEnum;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import com.techchallenge4.ms_pedido.service.PedidoServiceInterno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PEDIDO_NAO_ENCONTRADO;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceInternoImpl implements PedidoServiceInterno {

    private final PedidoRepository pedidoRepository;

    private final PedidoResponseAdapter pedidoResponseAdapter;

    @Override
    public List<PedidoResponse> listarPedidosPorStatusEUf(PedidoStatus status,
                                                          UfEnum uf) {

        log.info("estado=init metodo=PedidoServiceImpl.listarPedidosPorUfEStatus");
        var pedidos = pedidoRepository.findAllByStatusAndEndereco_Uf(status, uf);

        log.info("estado=finish metodo=PedidoServiceImpl.listarPedidosPorUfEStatus");
        return pedidoResponseAdapter.buildPedidoResponse(pedidos);
    }

    @Override
    public PedidoResponse buscarPorId(Long id) {
        log.info("estado=init metodo=PedidoServiceImpl.buscarPorId");
        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "buscarPorId", "/pedidos/{id}"));

        log.info("estado=finish metodo=PedidoServiceImpl.buscarPorId");
        return pedidoResponseAdapter.buildPedidoResponse(pedido);
    }

    @Override
    public PedidoResponse atualizarStatus(Long id, PedidoStatus status) {
        log.info("estado=init metodo=PedidoServiceImpl.atualizarStatus");
        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "atualizarStatus", "/pedidos/{id}/status/{status}"));

        pedido.setStatus(status);

        pedido = pedidoRepository.save(pedido);

        log.info("estado=finish metodo=PedidoServiceImpl.atualizarStatus");
        return pedidoResponseAdapter.buildPedidoResponse(pedido);
    }

}
