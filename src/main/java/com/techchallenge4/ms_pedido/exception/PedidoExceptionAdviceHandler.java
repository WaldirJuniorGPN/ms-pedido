package com.techchallenge4.ms_pedido.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class PedidoExceptionAdviceHandler {

    @ExceptionHandler(PedidoExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleException(PedidoExceptionHandler e) {
        var errorResponse = new ErrorResponse(
                LocalDateTime.now(), e.getPedidoErrorCode().getHttpStatus(), e.getMessage(), e.getPath(), e.getMetodo());

        if (e.getPedidoErrorCode().isExibirException()) {
            log.error("", e);
        } else {
            log.error("Error: {}", errorResponse);
        }

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        var errorMessage = new ErrorResponse(LocalDateTime.now(), BAD_REQUEST, errors.toString());
        log.error("Error: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException.NotFound ex) {
        var errorMessage = new ErrorResponse(LocalDateTime.now(), NOT_FOUND, ex.getMessage());
        log.error("Error: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

}
