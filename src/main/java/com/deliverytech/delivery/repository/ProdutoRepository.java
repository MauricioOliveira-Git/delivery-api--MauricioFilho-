package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.deliverytech.delivery.entity.ProdutoEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findByRestaurantId(Long restaurantId);

    List<ProdutoEntity> fingByRestaurantIdAndActiveTrue(long restaurantId);

    List<ProdutoEntity> findByCategoryAndActiveTrue(String category);

    @Query("SELECT pe FROM ProdutoEntity pe WHERE pe.active = true AND pe.available = true")
    List<ProdutoEntity> findProductAvailable();

    @Query("SELECT pe FROM ProdutoEntity pe WHERE pe.restaurant.id = :restaurantId AND pe.category = :category AND pe.active = true AND pe.available = true")
    List<ProdutoEntity> findByRestaurantAndCategory(@Param("restaurantId") Long restaurantId, @Param("category") String category);
}
