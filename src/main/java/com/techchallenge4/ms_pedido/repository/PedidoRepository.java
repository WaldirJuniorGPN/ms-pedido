package com.techchallenge4.ms_pedido.repository;

import com.techchallenge4.ms_pedido.model.Pedido;
import com.techchallenge4.ms_pedido.model.enums.PedidoStatus;
import com.techchallenge4.ms_pedido.model.enums.UfEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAllByClienteId(Long clienteId, PageRequest pageRequest);

    List<Pedido> findAllByStatusAndEndereco_Uf(PedidoStatus status, UfEnum ufEnum);

    Optional<Pedido> findByIdAndClienteId(Long id, Long clientId);
}
