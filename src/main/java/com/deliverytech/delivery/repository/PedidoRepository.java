package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.PedidoEntity;
import com.deliverytech.delivery.entity.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByClientId(long clientId);

    List<PedidoEntity> findByStatus(StatusPedido status);

    List<PedidoEntity> findByClientIdAndStatus(long clientId, StatusPedido status);

    List<PedidoEntity> findByCreationDateAfter(LocalDateTime data);

    @Query("SELECT p FROM PedidoEntity p WHERE p.creationDate BETWEEN :startDate AND :endDate")
    List<PedidoEntity> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
}