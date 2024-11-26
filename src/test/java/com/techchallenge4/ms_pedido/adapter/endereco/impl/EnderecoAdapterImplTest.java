package com.techchallenge4.ms_pedido.adapter.endereco.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static com.techchallenge4.ms_pedido.mockDados.ClienteResponseMock.getClienteResponse;
import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;
import static org.mockito.MockitoAnnotations.openMocks;

class EnderecoAdapterImplTest {

    @InjectMocks
    private EnderecoAdapterImpl enderecoAdapterImpl;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


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