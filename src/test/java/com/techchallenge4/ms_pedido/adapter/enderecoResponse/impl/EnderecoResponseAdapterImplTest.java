package com.techchallenge4.ms_pedido.adapter.enderecoResponse.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.techchallenge4.ms_pedido.mockDados.EnderecoMock.getEndereco1;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;

@ExtendWith(MockitoExtension.class)
class EnderecoResponseAdapterImplTest {

    @InjectMocks
    private EnderecoResponseAdapterImpl enderecoResponseAdapterImpl;

    @Test
    void testaBuildEnderecoResponse() {
        var endereco = getEndereco1();

        var resultado = enderecoResponseAdapterImpl.buildEnderecoResponse(endereco);

        Assertions.assertAll("Endereço Response", () -> {
            Assertions.assertEquals("Rua 1", resultado.logradouro());
            Assertions.assertEquals("123", resultado.numero());
            Assertions.assertEquals("casa", resultado.complemento());
            Assertions.assertEquals("Bairro 1", resultado.bairro());
            Assertions.assertEquals("Cidade 1", resultado.cidade());
            Assertions.assertEquals(PE, resultado.uf());
            Assertions.assertEquals("12345678", resultado.cep());
            Assertions.assertEquals("321321", resultado.latitude());
            Assertions.assertEquals("123123", resultado.longitude());
        });
    }

}