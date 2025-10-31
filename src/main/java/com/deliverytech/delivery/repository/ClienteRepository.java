package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.ClienteEntity;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByEmail(String email);

    List<ClienteEntity> findByActiveTrue();

    List<ClienteEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM ClienteEntity c WHERE c.email = :email AND c.active = true")
    Optional<ClienteEntity> findbyEmailAndActive(@Param("email") String email);
}
