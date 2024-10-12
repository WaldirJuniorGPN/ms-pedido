package com.techchallenge4.ms_pedido.repository;

import com.techchallenge4.ms_pedido.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAllByClienteId(Long clienteId, PageRequest pageRequest);

}
