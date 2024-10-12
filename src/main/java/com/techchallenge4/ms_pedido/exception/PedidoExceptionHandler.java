package com.techchallenge4.ms_pedido.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PedidoExceptionHandler extends RuntimeException {

    private static final long serialVersionUID = -3326368034777406990L;

    private final PedidoErrorCode pedidoErrorCode;
    private final String metodo;
    private final String path;

    public PedidoExceptionHandler(PedidoErrorCode pedidoErrorCode, String metodo, String path) {
        super(pedidoErrorCode.getMensagem());
        this.pedidoErrorCode = pedidoErrorCode;
        this.metodo = metodo;
        this.path = path;
    }

    public HttpStatus getHttpStatus() {
        return pedidoErrorCode.getHttpStatus();
    }

}
