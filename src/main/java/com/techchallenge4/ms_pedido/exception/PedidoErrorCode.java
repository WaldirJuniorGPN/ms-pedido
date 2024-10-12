package com.techchallenge4.ms_pedido.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum PedidoErrorCode {

    CLIENTE_NAO_ENCONTRADO("Cliente não encontrado", NOT_FOUND, false),

    PRODUTO_NAO_ENCOTRADO("Produto não encontrado", NOT_FOUND, false),
    PRODUTO_SEM_ESTOQUE("Produto sem estoque", NOT_FOUND, false),

    PEDIDO_NAO_ENCONTRADO("Pedido não encontrado", NOT_FOUND, false),

    ERROR_DESCONHECIDO("Erro desconhecido", INTERNAL_SERVER_ERROR, true);

    private final String mensagem;
    private final HttpStatus httpStatus;
    private final boolean exibirException;
    private final int codigo;

    PedidoErrorCode(String mensagem, HttpStatus httpStatus, boolean exibirException) {
        this.mensagem = mensagem;
        this.httpStatus = httpStatus;
        this.exibirException = exibirException;
        this.codigo = httpStatus.value();
    }

}
