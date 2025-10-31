package com.deliverytech.delivery.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.entity.RestauranteEntity;
import com.deliverytech.delivery.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody RestauranteEntity restaurante) {
        try {
            RestauranteEntity savedRestaurant = restauranteService.registerRestaurant(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RestauranteEntity>> getAllActiveRestaurants() {
        List<RestauranteEntity> restaurants = restauranteService.listActiveRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        Optional<RestauranteEntity> restaurant = restauranteService.searchById(id);
        
        if (restaurant.isPresent()) {
            return ResponseEntity.ok(restaurant.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Restaurante não encontrado com o ID: " + id);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<RestauranteEntity>> getRestaurantsByCategory(@PathVariable String category) {
        List<RestauranteEntity> restaurants = restauranteService.searchByCategory(category);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestauranteEntity>> searchRestaurantsByName(@RequestParam String name) {
        List<RestauranteEntity> restaurants = restauranteService.searchByName(name);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search/term")
    public ResponseEntity<List<RestauranteEntity>> searchRestaurantsByNameOrCategory(@RequestParam String term) {
        List<RestauranteEntity> restaurants = restauranteService.searchByNameOrCategory(term);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/top")
    public ResponseEntity<List<RestauranteEntity>> getTopRestaurants() {
        List<RestauranteEntity> restaurants = restauranteService.listBestRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody RestauranteEntity updatedRestaurant) {
        try {
            RestauranteEntity restaurant = restauranteService.updateRestaurant(id, updatedRestaurant);
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateRestaurant(@PathVariable Long id) {
        try {
            restauranteService.deactivateRestaurant(id);
            return ResponseEntity.ok("Restaurante desativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateRestaurant(@PathVariable Long id) {
        try {
            restauranteService.activateRestaurant(id);
            return ResponseEntity.ok("Restaurante ativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/rating")
    public ResponseEntity<?> updateRating(@PathVariable Long id, @RequestParam Double rating) {
        try {
            restauranteService.updateRating(id, rating);
            return ResponseEntity.ok("Avaliação atualizada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}