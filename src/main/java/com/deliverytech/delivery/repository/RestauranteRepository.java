package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery.entity.RestauranteEntity;

@Repository
public interface RestauranteRepository extends JpaRepository<RestauranteEntity, Long> {

    List<RestauranteEntity> findByNameContainingIgnoraCamelCase(String name);

    List<RestauranteEntity> fingByCategory(String category);

    List<RestauranteEntity> findByActiveTrue();

    List<RestauranteEntity> findByCategoryActiveTrue(String category);

    @Query("SELECT r FROM RestauranteEntity r WHERE r.active = true ORDER BY r.rating DESC")
    List<RestauranteEntity> findBestRestaurant();

    @Query("SELECT r FROM RestauranteEntity r WHERE r.active = true AND (LOWER(r.name) LIKE LOWER (CONCAT('%', :term, '%')) OR LOWER(r.category) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<RestauranteEntity> searchForNameOrCategory(@Param("term") String term);
}
