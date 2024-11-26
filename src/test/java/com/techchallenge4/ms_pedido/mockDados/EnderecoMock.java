package com.techchallenge4.ms_pedido.mockDados;

import com.techchallenge4.ms_pedido.model.Endereco;

import static com.techchallenge4.ms_pedido.model.enums.UfEnum.PE;

public interface EnderecoMock {

    static Endereco getEndereco1() {
        return Endereco.builder()
                .id(1L)
                .logradouro("Rua 1")
                .numero("123")
                .complemento("casa")
                .bairro("Bairro 1")
                .cidade("Cidade 1")
                .uf(PE)
                .cep("12345678")
                .latitude("321321")
                .longitude("123123")
                .build();
    }

    static Endereco getEndereco2() {
        return Endereco.builder()
                .id(2L)
                .logradouro("Rua 2")
                .numero("456")
                .complemento("apto")
                .bairro("Bairro 2")
                .cidade("Cidade 2")
                .uf(PE)
                .cep("87654321")
                .latitude("321321")
                .longitude("123123")
                .build();
    }

}
