package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.connectors.produto.ConexaoProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.connectors.usuario.ConexaoCliente;
import com.techchallenge4.ms_pedido.connectors.usuario.response.ClienteResponse;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import com.techchallenge4.ms_pedido.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PEDIDO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_SEM_ESTOQUE;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PREPARANDO;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final ConexaoCliente conexaoCliente;

    private final ConexaoProduto conexaoProduto;

    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public PedidoResponse create(PedidoRequest request) {

        var clienteResponse = conexaoCliente.buscarClientePorId(request.usuarioId());

        var produtoResponse = conexaoProduto.buscarProdutoPorId(request.produtoId());
        if (produtoResponse.quantidadeEstoque() <= 0 || getQuantidadeTotal(request, produtoResponse) < 0) {
            throw new PedidoExceptionHandler(PRODUTO_SEM_ESTOQUE, "create", "/pedidos");
        }

        var pedido = buildPedido(request, clienteResponse, produtoResponse);

        pedido = pedidoRepository.save(pedido);

        var pedidoResponse = buildPedidoResponse(pedido, clienteResponse, produtoResponse);

        conexaoProduto.atualizarQuantidadeEstoqueProduto(produtoResponse.id(), request.quantidade());

        return pedidoResponse;
    }

    @Override
    public PagedModel<PedidoResponse> listarTodos(int pagina, int tamanho) {

        var pedidos = pedidoRepository.findAll(PageRequest.of(pagina, tamanho));

        return new PagedModel<>(buildPedidoResponse(pedidos));
    }

    @Override
    public PedidoResponse buscarPorId(Long id) {

        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "buscarPorId", "/pedidos/{id}"));

        return buildPedidoResponse(pedido);
    }

    @Override
    public PedidoResponse atualizarStatus(Long id, PedidoStatus status) {

        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "atualizarStatus", "/pedidos/{id}/status/{status}"));

        pedido.setStatus(status);

        pedido = pedidoRepository.save(pedido);

        return buildPedidoResponse(pedido);
    }

    @Override
    public PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho) {

        var pedidos = pedidoRepository.findAllByClienteId(clienteId, PageRequest.of(pagina, tamanho));

        return new PagedModel<>(buildPedidoResponse(pedidos));
    }

    private static PedidoResponse buildPedidoResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .produtoId(pedido.getProdutoId())
                .quantidade(pedido.getQuantidade())
                .dataHoraCriacao(pedido.getDataHoraCriacao())
                .status(pedido.getStatus())
                .build();
    }

    private static Page<PedidoResponse> buildPedidoResponse(Page<Pedido> pedidos) {
        return pedidos.map(PedidoServiceImpl::buildPedidoResponse);
    }

    private static PedidoResponse buildPedidoResponse(Pedido pedido, ClienteResponse clienteResponse, ProdutoResponse produtoResponse) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(clienteResponse.id())
                .produtoId(produtoResponse.id())
                .quantidade(pedido.getQuantidade())
                .dataHoraCriacao(pedido.getDataHoraCriacao())
                .status(pedido.getStatus())
                .build();
    }

    private static Pedido buildPedido(PedidoRequest request, ClienteResponse clienteResponse, ProdutoResponse produtoResponse) {
        return Pedido.builder()
                .clienteId(clienteResponse.id())
                .produtoId(produtoResponse.id())
                .quantidade(request.quantidade())
                .dataHoraCriacao(LocalDateTime.now())
                .status(PREPARANDO)
                .build();
    }

    private static int getQuantidadeTotal(PedidoRequest request, ProdutoResponse produtoResponse) {
        return produtoResponse.quantidadeEstoque() - request.quantidade();
    }
}
