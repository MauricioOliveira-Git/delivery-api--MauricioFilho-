package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.dto.RelatorioProjection;
import com.deliverytech.delivery.entity.ProdutoEntity;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findByRestaurantId(Long restaurantId);

    List<ProdutoEntity> findByRestaurantIdAndActiveTrue(long restaurantId);

    List<ProdutoEntity> findByCategoryAndActiveTrue(String category);

    List<ProdutoEntity> findByActiveTrue();


    @Query("SELECT pe FROM ProdutoEntity pe WHERE pe.active = true AND pe.available = true")
    List<ProdutoEntity> findProductAvailable();

    @Query("SELECT pe FROM ProdutoEntity pe WHERE pe.restaurant.id = :restaurantId AND pe.category = :category AND pe.active = true AND pe.available = true")
    List<ProdutoEntity> findByRestaurantAndCategory(@Param("restaurantId") Long restaurantId, @Param("category") String category);

    @Query("SELECT ip.product.name, SUM(ip.quantity) as totalVendido "
            + "FROM ItemPedidoEntity ip "
            + "GROUP BY ip.product.id, ip.product.name "
            + "ORDER BY totalVendido DESC")
    List<Object[]> findProdutosMaisVendidos();

    @Query(value
            = "SELECT pr.name as nomeProduto, SUM(ip.quantity) as totalVendido, "
            + "       pr.price as preco, r.name as restaurante "
            + "FROM produtos pr "
            + "JOIN itens_pedido ip ON pr.id = ip.product_id "
            + "JOIN restaurantes r ON pr.restaurant_id = r.id "
            + "GROUP BY pr.id, pr.name, pr.price, r.name "
            + "ORDER BY totalVendido DESC "
            + "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findTop10ProdutosMaisVendidos();

    @Query("SELECT p.name as nomeProduto, p.category as categoria, "
            + "SUM(ip.quantity) as totalVendido, "
            + "SUM(ip.quantity * ip.unitPrice) as faturamentoTotal "
            + "FROM ProdutoEntity p "
            + "JOIN ItemPedidoEntity ip ON p.id = ip.product.id "
            + "GROUP BY p.id, p.name, p.category "
            + "ORDER BY totalVendido DESC")
    List<RelatorioProjection.ProdutosMaisVendidos> findProdutosMaisVendidosComProjecao();
}
