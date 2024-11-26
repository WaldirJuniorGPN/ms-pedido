package com.techchallenge4.ms_pedido.exception;

import feign.FeignException;
import feign.Request;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.ERROR_DESCONHECIDO;
import static com.techchallenge4.ms_pedido.exception.PedidoErrorCode.PEDIDO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoExceptionAdviceHandlerTest {

    @InjectMocks
    private PedidoExceptionAdviceHandler pedidoExceptionAdviceHandler;

    LocalDateTime now = LocalDateTime.parse("2021-08-01T00:00:00");

    @Test
    void testaHandleExceptionNaoExibirException() {

        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            PedidoExceptionHandler pedidoExceptionHandler = new PedidoExceptionHandler(PEDIDO_NAO_ENCONTRADO, "/pedidos", "/buscarPorId");

            pedidoExceptionAdviceHandler.handleException(pedidoExceptionHandler);
        }

    }

    @Test
    void testaHandleExceptionExibirException() {

        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(now);

            PedidoExceptionHandler pedidoExceptionHandler = new PedidoExceptionHandler(ERROR_DESCONHECIDO, "/pedidos", "/buscarPorId");

            pedidoExceptionAdviceHandler.handleException(pedidoExceptionHandler);
        }

    }

    @Test
    void TestaHandleValidationExceptions() {
        var bindingResult = mock(BindingResult.class);
        var ex = new MethodArgumentNotValidException(null, bindingResult);
        var fieldError = new FieldError("objectName", "field", "defaultMessage");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        var response = pedidoExceptionAdviceHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMensagem().contains("field"));
        assertTrue(response.getBody().getMensagem().contains("defaultMessage"));
    }

    @Test
    void testaHandleFeignExceptionNotFound() {
        var request = Request.create(Request.HttpMethod.GET, "/path", Map.of(), null, Util.UTF_8);
        var ex = new FeignException.NotFound("Not Found", request, null, null);

        var response = pedidoExceptionAdviceHandler.handleFeignException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getMensagem());
    }

}