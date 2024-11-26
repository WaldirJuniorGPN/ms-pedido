package com.techchallenge4.ms_pedido.adapter.endereco.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.techchallenge4.ms_pedido.mockDados.ClienteResponseMock.getClienteResponse;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;

@ExtendWith(MockitoExtension.class)
class EnderecoAdapterImplTest {

    @InjectMocks
    private EnderecoAdapterImpl enderecoAdapterImpl;


    @Test
    void testaBuildEndereco() {

        var response = getClienteResponse();

        var resultado = enderecoAdapterImpl.buildEndereco(response);

        Assertions.assertAll("Endereço", ()-> {
            Assertions.assertEquals("123", resultado.getCep());
            Assertions.assertEquals(PE, resultado.getUf());
            Assertions.assertEquals("Cidade 1", resultado.getCidade());
            Assertions.assertEquals("Bairro 1", resultado.getBairro());
            Assertions.assertEquals("casa", resultado.getComplemento());
            Assertions.assertEquals("Rua 1", resultado.getLogradouro());
            Assertions.assertEquals("123", resultado.getNumero());
            Assertions.assertEquals("123123", resultado.getLatitude());
            Assertions.assertEquals("321321", resultado.getLongitude());
        });

    }

}