package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivery.dto.RelatorioProjection;

import com.deliverytech.delivery.entity.PedidoEntity;
import com.deliverytech.delivery.entity.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByClientId(long clientId);

    List<PedidoEntity> findByStatus(StatusPedido status);

    List<PedidoEntity> findByClientIdAndStatus(long clientId, StatusPedido status);

    List<PedidoEntity> findByCreationDateAfter(LocalDateTime data);

    List<PedidoEntity> findTop10ByOrderByIdDesc();

    @Query("SELECT p FROM PedidoEntity p WHERE p.creationDate BETWEEN :startDate AND :endDate")
    List<PedidoEntity> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.restaurant.name, SUM(p.totalAmount) "
            + "FROM PedidoEntity p "
            + "GROUP BY p.restaurant.id, p.restaurant.name "
            + "ORDER BY SUM(p.totalAmount) DESC")
    List<Object[]> findTotalVendasPorRestaurante();

    @Query("SELECT p FROM PedidoEntity p WHERE p.totalAmount > :valorMinimo ORDER BY p.totalAmount DESC")
    List<PedidoEntity> findPedidosComValorAcimaDe(@Param("valorMinimo") BigDecimal valorMinimo);

    @Query("SELECT p FROM PedidoEntity p "
            + "WHERE p.creationDate BETWEEN :dataInicio AND :dataFim "
            + "AND p.status = :status "
            + "ORDER BY p.creationDate DESC")
    List<PedidoEntity> findPedidosPorPeriodoEStatus(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") StatusPedido status);

    @Query(value
            = "SELECT c.name as nomeCliente, COUNT(p.id) as totalPedidos "
            + "FROM clientes c "
            + "LEFT JOIN pedidos p ON c.id = p.client_id "
            + "GROUP BY c.id, c.name "
            + "ORDER BY totalPedidos DESC",
            nativeQuery = true)
    List<Object[]> findRankingClientesPorPedidos();

    @Query(value
            = "SELECT r.category as categoria, SUM(p.total_amount) as faturamento "
            + "FROM restaurantes r "
            + "JOIN pedidos p ON r.id = p.restaurant_id "
            + "GROUP BY r.category "
            + "ORDER BY faturamento DESC",
            nativeQuery = true)
    List<Object[]> findFaturamentoPorCategoria();

    @Query("SELECT p.restaurant.name as restaurante, "
            + "SUM(p.totalAmount) as totalVendas, "
            + "COUNT(p.id) as totalPedidos "
            + "FROM PedidoEntity p "
            + "GROUP BY p.restaurant.id, p.restaurant.name "
            + "ORDER BY totalVendas DESC")
    List<RelatorioProjection.VendasPorRestaurante> findVendasPorRestauranteComProjecao();

    @Query("SELECT c.name as nomeCliente, c.email as email, "
            + "COUNT(p.id) as totalPedidos, SUM(p.totalAmount) as totalGasto "
            + "FROM ClienteEntity c "
            + "LEFT JOIN PedidoEntity p ON c.id = p.client.id "
            + "GROUP BY c.id, c.name, c.email "
            + "ORDER BY totalPedidos DESC, totalGasto DESC")
    List<RelatorioProjection.RankingClientes> findRankingClientesComProjecao();

}
