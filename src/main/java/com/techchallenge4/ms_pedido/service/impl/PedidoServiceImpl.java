package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.adapter.endereco.EnderecoAdapter;
import com.techchallenge4.ms_pedido.adapter.pedido.PedidoAdapter;
import com.techchallenge4.ms_pedido.adapter.pedidoResponse.PedidoResponseAdapter;
import com.techchallenge4.ms_pedido.connectors.cliente.ConexaoCliente;
import com.techchallenge4.ms_pedido.connectors.produto.ConexaoProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.dto.PedidoDTO;
import com.techchallenge4.ms_pedido.repository.EnderecoRepository;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import com.techchallenge4.ms_pedido.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_QUEUE_CRIAR;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.CLIENTES_DIVERGENTES;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PEDIDO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_SEM_ESTOQUE;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.CANCELADA;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoResponseAdapter pedidoResponseAdapter;

    private final EnderecoAdapter enderecoAdapter;

    private final PedidoAdapter pedidoAdapter;

    private final ConexaoCliente conexaoCliente;

    private final ConexaoProduto conexaoProduto;

    private final PedidoRepository pedidoRepository;

    private final EnderecoRepository enderecoRepository;

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void recebe(Long clienteId, PedidoRequest request) {
        log.info("estado=init metodo=PedidoServiceImpl.recebe");

        validaClienteLogado(clienteId, request, "recebe");

        var clienteResponse = conexaoCliente.buscarClientePorId(request.clienteId());

        var produtoResponse = conexaoProduto.buscarProdutoPorId(request.produtoId());

        validacaoProdutoDisponivelEstoque(request, produtoResponse, "recebe");

        var pedidoDTO = new PedidoDTO(request, clienteResponse, produtoResponse);

        rabbitTemplate.convertAndSend(PEDIDO_QUEUE_CRIAR, pedidoDTO);
        log.info("estado=finish metodo=PedidoServiceImpl.recebe");
    }

    @Override
    @Transactional
    public void salvar(PedidoDTO pedidoDTO) {
        log.info("estado=init metodo=PedidoServiceImpl.salvar");

        var pedido = pedidoAdapter.buildPedido(pedidoDTO);

        var endereco = enderecoAdapter.buildEndereco(pedidoDTO.getClienteResponse());

        endereco = enderecoRepository.save(endereco);
        pedido.setEndereco(endereco);

        pedidoRepository.save(pedido);

        conexaoProduto.atualizarQuantidadeEstoqueProduto(pedidoDTO.getProdutoResponse().id(), pedidoDTO.getPedidoRequest().quantidade());

        log.info("estado=finish metodo=PedidoServiceImpl.salvar");
    }

    @Override
    public PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho) {
        log.info("estado=init metodo=PedidoServiceImpl.listarPorCliente");
        var pedidos = pedidoRepository.findAllByClienteId(clienteId, PageRequest.of(pagina, tamanho));

        log.info("estado=finish metodo=PedidoServiceImpl.listarPorCliente");
        return new PagedModel<>(pedidoResponseAdapter.buildPedidoResponsePaginado(pedidos));
    }

    @Override
    public PedidoResponse cancelar(Long id, Long clientId) {
        log.info("estado=init metodo=PedidoServiceImpl.cancelar");

        var pedido = pedidoRepository.findByIdAndClienteId(id, clientId).orElseThrow(
                () -> new PedidoExceptionHandler(PEDIDO_NAO_ENCONTRADO, "cancelar", "/pedidos"));
        pedido.setStatus(CANCELADA);

        pedido = pedidoRepository.save(pedido);

        log.info("estado=finish metodo=PedidoServiceImpl.cancelar");
        return pedidoResponseAdapter.buildPedidoResponse(pedido);
    }

    private void validaClienteLogado(Long clienteId, PedidoRequest request, String metodo) {
        if (!clienteId.equals(request.clienteId())) {
            throw new PedidoExceptionHandler(CLIENTES_DIVERGENTES, metodo, "/pedidos");
        }
    }

    private void validacaoProdutoDisponivelEstoque(PedidoRequest request, ProdutoResponse produtoResponse, String metodo) {
        if (produtoResponse.quantidadeEstoque() <= 0 || getQuantidadeTotal(request, produtoResponse) < 0) {
            throw new PedidoExceptionHandler(PRODUTO_SEM_ESTOQUE, metodo, "/pedidos");
        }
    }

    private int getQuantidadeTotal(PedidoRequest request, ProdutoResponse produtoResponse) {
        return produtoResponse.quantidadeEstoque() - request.quantidade();
    }
}
