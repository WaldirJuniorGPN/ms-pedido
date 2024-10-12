package com.techchallenge4.ms_pedido.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime dataHora;
    private HttpStatus httpStatus;
    private String mensagem;
    private String path;
    private String metodo;
    private int codigo;

    public ErrorResponse(LocalDateTime dataHora, HttpStatus httpStatus, String mensagem, String path, String metodo) {
        this.dataHora = dataHora;
        this.httpStatus = httpStatus;
        this.mensagem = mensagem;
        this.path = path;
        this.metodo = metodo;
        this.codigo = httpStatus.value();
    }

    public ErrorResponse(LocalDateTime dataHora, HttpStatus httpStatus, String mensagem) {
        this.dataHora = dataHora;
        this.httpStatus = httpStatus;
        this.mensagem = mensagem;
        this.codigo = httpStatus.value();
    }

}
