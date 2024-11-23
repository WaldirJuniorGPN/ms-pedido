package com.techchallenge4.ms_pedido.service.impl;

import com.techchallenge4.ms_pedido.connectors.produto.ConexaoProduto;
import com.techchallenge4.ms_pedido.connectors.produto.response.ProdutoResponse;
import com.techchallenge4.ms_pedido.connectors.cliente.ConexaoCliente;
import com.techchallenge4.ms_pedido.connectors.cliente.response.ClienteResponse;
import com.techchallenge4.ms_pedido.connectors.cliente.response.EnderecoResponse;
import com.techchallenge4.ms_pedido.controller.request.PedidoRequest;
import com.techchallenge4.ms_pedido.controller.response.PedidoResponse;
import com.techchallenge4.ms_pedido.exception.PedidoExceptionHandler;
import com.techchallenge4.ms_pedido.model.Endereco;
import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.enums.EstadoEnum;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import com.techchallenge4.ms_pedido.repository.EnderecoRepository;
import com.techchallenge4.ms_pedido.repository.PedidoRepository;
import com.techchallenge4.ms_pedido.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.techchallenge4.ms_pedido.config.Environment.PEDIDO_QUEUE_CRIAR;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.CLIENTES_DIVERGENTES;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PEDIDO_NAO_ENCONTRADO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PRODUTO_SEM_ESTOQUE;
import static com.techchallenge4.ms_pedido.model.enums.PedidoStatus.PENDENTE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final ConexaoCliente conexaoCliente;

    private final ConexaoProduto conexaoProduto;

    private final PedidoRepository pedidoRepository;

    private final EnderecoRepository enderecoRepository;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void recebe(Long clienteId, PedidoRequest request) {
        log.info("estado=init metodo=PedidoServiceImpl.recebe");

        validaClienteLogado(clienteId, request);

        conexaoCliente.buscarClientePorId(request.clienteId());

        var produtoResponse = conexaoProduto.buscarProdutoPorId(request.produtoId());

        validacaoProdutoDisponivelEstoque(request, produtoResponse, "recebe");

        rabbitTemplate.convertAndSend(PEDIDO_QUEUE_CRIAR, request);
        log.info("estado=finish metodo=PedidoServiceImpl.recebe");
    }

    @Override
    @Transactional
    public void salvar(PedidoRequest request) {
        log.info("estado=init metodo=PedidoServiceImpl.salvar");
        var clienteResponse = conexaoCliente.buscarClientePorId(request.clienteId());

        var produtoResponse = conexaoProduto.buscarProdutoPorId(request.produtoId());
        validacaoProdutoDisponivelEstoque(request, produtoResponse, "salvar");

        var pedido = buildPedido(request, clienteResponse, produtoResponse);

        var endereco = buildEndereco(clienteResponse);

        endereco = enderecoRepository.save(endereco);
        pedido.setEndereco(endereco);

        pedidoRepository.save(pedido);

        conexaoProduto.atualizarQuantidadeEstoqueProduto(produtoResponse.id(), request.quantidade());

        log.info("estado=finish metodo=PedidoServiceImpl.salvar");
    }

    @Override
    public PagedModel<PedidoResponse> listarTodos(int pagina, int tamanho) {
        log.info("estado=init metodo=PedidoServiceImpl.listarTodos");
        var pedidos = pedidoRepository.findAll(PageRequest.of(pagina, tamanho));

        log.info("estado=finish metodo=PedidoServiceImpl.listarTodos");
        return new PagedModel<>(buildPedidoResponsePaginado(pedidos));
    }

    @Override
    public List<PedidoResponse> listarPedidosPorStatusEUf(PedidoStatus status,
                                                          EstadoEnum uf) {

        log.info("estado=init metodo=PedidoServiceImpl.listarPedidosPorUfEStatus");
        var pedidos = pedidoRepository.findAllByStatusAndEndereco_Uf(status, uf);

        log.info("estado=finish metodo=PedidoServiceImpl.listarPedidosPorUfEStatus");
        return buildPedidoResponse(pedidos);
    }

    @Override
    public PedidoResponse buscarPorId(Long id) {
        log.info("estado=init metodo=PedidoServiceImpl.buscarPorId");
        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "buscarPorId", "/pedidos/{id}"));

        log.info("estado=finish metodo=PedidoServiceImpl.buscarPorId");
        return buildPedidoResponse(pedido);
    }

    @Override
    public PedidoResponse atualizarStatus(Long id, PedidoStatus status) {
        log.info("estado=init metodo=PedidoServiceImpl.atualizarStatus");
        var pedido = pedidoRepository.findById(id).orElseThrow(() -> new PedidoExceptionHandler(
                PEDIDO_NAO_ENCONTRADO, "atualizarStatus", "/pedidos/{id}/status/{status}"));

        pedido.setStatus(status);

        pedido = pedidoRepository.save(pedido);
        var pedidoResponse = buildPedidoResponse(pedido);

        log.info("estado=finish metodo=PedidoServiceImpl.atualizarStatus");
        return pedidoResponse;
    }

    @Override
    public PagedModel<PedidoResponse> listarPorCliente(Long clienteId, int pagina, int tamanho) {
        log.info("estado=init metodo=PedidoServiceImpl.listarPorCliente");
        var pedidos = pedidoRepository.findAllByClienteId(clienteId, PageRequest.of(pagina, tamanho));

        log.info("estado=finish metodo=PedidoServiceImpl.listarPorCliente");
        return new PagedModel<>(buildPedidoResponsePaginado(pedidos));
    }

    private void validaClienteLogado(Long clienteId, PedidoRequest request) {
        if (!clienteId.equals(request.clienteId())) {
            throw new PedidoExceptionHandler(CLIENTES_DIVERGENTES, "recebe", "/pedidos");
        }
    }

    private void validacaoProdutoDisponivelEstoque(PedidoRequest request, ProdutoResponse produtoResponse, String metodo) {
        if (produtoResponse.quantidadeEstoque() <= 0 || getQuantidadeTotal(request, produtoResponse) < 0) {
            throw new PedidoExceptionHandler(PRODUTO_SEM_ESTOQUE, metodo, "/pedidos");
        }
    }

    private static PedidoResponse buildPedidoResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .produtoId(pedido.getProdutoId())
                .quantidade(pedido.getQuantidade())
                .dataHoraCriacao(pedido.getDataHoraCriacao())
                .endereco(buildEnderecoResponse(pedido.getEndereco()))
                .status(pedido.getStatus())
                .build();
    }

    private static Page<PedidoResponse> buildPedidoResponsePaginado(Page<Pedido> pedidosPaginados) {
        return pedidosPaginados.map(PedidoServiceImpl::buildPedidoResponse);
    }

    private static List<PedidoResponse> buildPedidoResponse(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoServiceImpl::buildPedidoResponse).toList();
    }

    private Pedido buildPedido(PedidoRequest request, ClienteResponse clienteResponse, ProdutoResponse produtoResponse) {
        return Pedido.builder()
                .clienteId(clienteResponse.id())
                .produtoId(produtoResponse.id())
                .quantidade(request.quantidade())
                .dataHoraCriacao(LocalDateTime.now())
                .status(PENDENTE)
                .build();
    }

    private Endereco buildEndereco(ClienteResponse clienteResponse) {
        return Endereco.builder()
                .cep(clienteResponse.endereco().cep())
                .uf(clienteResponse.endereco().estado())
                .cidade(clienteResponse.endereco().cidade())
                .bairro(clienteResponse.endereco().bairro())
                .complemento(clienteResponse.endereco().complemento())
                .logradouro(clienteResponse.endereco().logradouro())
                .numero(clienteResponse.endereco().numero())
                .latitude(clienteResponse.endereco().latitude())
                .logitude(clienteResponse.endereco().longitude())
                .build();
    }

    private static EnderecoResponse buildEnderecoResponse(Endereco endereco) {
        return new EnderecoResponse(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(),
                endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getLatitude(),
                endereco.getLogitude(), endereco.getUf());
    }

    private int getQuantidadeTotal(PedidoRequest request, ProdutoResponse produtoResponse) {
        return produtoResponse.quantidadeEstoque() - request.quantidade();
    }
}
