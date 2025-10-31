package com.deliverytech.delivery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.RestauranteEntity;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    // CREATE
    @Transactional
    public RestauranteEntity registerRestaurant(RestauranteEntity restaurant) {
        if (restaurant.getDeliveryTime() <= 0) {
            throw new RuntimeException("O prazo de entrega deve ser maior que zero.");
        }
        
        if (restaurant.getRating() < 0 || restaurant.getRating() > 5) {
            throw new RuntimeException("A classificação deve estar entre 0 e 5.");
        }
        
        return restauranteRepository.save(restaurant);
    }

    // READ
    public Optional<RestauranteEntity> searchById(Long id) {
        return restauranteRepository.findById(id);
    }

    public List<RestauranteEntity> listActiveRestaurants() {
        return restauranteRepository.findByActiveTrue();
    }

    public List<RestauranteEntity> searchByCategory(String category) {
        return restauranteRepository.findByCategoryAndActiveTrue(category);
    }

    public List<RestauranteEntity> searchByName(String name) {
        return restauranteRepository.findByNameContainingIgnoreCase(name);
    }

    public List<RestauranteEntity> searchByNameOrCategory(String term) {
        return restauranteRepository.searchForNameOrCategory(term);
    }

    public List<RestauranteEntity> listBestRestaurants() {
        return restauranteRepository.findBestRestaurant();
    }

    // UPDATE
    @Transactional
    public RestauranteEntity updateRestaurant(Long id, RestauranteEntity updatedRestaurant) {
        RestauranteEntity existingRestaurant = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com o ID: " + id));
        
        existingRestaurant.setName(updatedRestaurant.getName());
        existingRestaurant.setCategory(updatedRestaurant.getCategory());
        existingRestaurant.setAddress(updatedRestaurant.getAddress());
        existingRestaurant.setPhone(updatedRestaurant.getPhone());
        existingRestaurant.setDeliveryTime(updatedRestaurant.getDeliveryTime());
        existingRestaurant.setRating(updatedRestaurant.getRating());
        
        return restauranteRepository.save(existingRestaurant);
    }

    // DELETE
    @Transactional
    public void deactivateRestaurant(Long id) {
        RestauranteEntity restaurant = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com o ID: " + id));
        
        restaurant.setActive(false);
        restauranteRepository.save(restaurant);
    }

    // ACTIVATE
    @Transactional
    public void activateRestaurant(Long id) {
        RestauranteEntity restaurant = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com o ID: " + id));
        
        restaurant.setActive(true);
        restauranteRepository.save(restaurant);
    }

    @Transactional
    public void updateRating(Long id, Double newRating) {
        if (newRating < 0 || newRating > 5) {
            throw new RuntimeException("A classificação deve estar entre 0 e 5.");
        }
        
        RestauranteEntity restaurant = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com o ID: " + id));
        
        restaurant.setRating(newRating);
        restauranteRepository.save(restaurant);
    }
}