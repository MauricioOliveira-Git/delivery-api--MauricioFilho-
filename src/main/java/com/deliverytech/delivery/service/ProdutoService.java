package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.ProdutoEntity;
import com.deliverytech.delivery.entity.RestauranteEntity;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    // CREATE
    @Transactional
    public ProdutoEntity registerProduct(Long restaurantId, ProdutoEntity product) {
        RestauranteEntity restaurant = restauranteRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com o ID: " + restaurantId));
        
        if (!restaurant.getActive()) {
            throw new RuntimeException("O restaurante está inativo");
        }
        
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O preço do produto deve ser maior que zero.");
        }
        
        product.setRestaurant(restaurant);
        return produtoRepository.save(product);
    }

    // READ
    public Optional<ProdutoEntity> searchById(Long id) {
        return produtoRepository.findById(id);
    }

    public List<ProdutoEntity> listProductsByRestaurant(Long restaurantId) {
        return produtoRepository.findByRestaurantIdAndActiveTrue(restaurantId);
    }

    public List<ProdutoEntity> listAvailableProductsByRestaurant(Long restaurantId) {
        List<ProdutoEntity> products = produtoRepository.findByRestaurantIdAndActiveTrue(restaurantId);
        return products.stream()
            .filter(ProdutoEntity::getAvailable)
            .toList();
    }

    public List<ProdutoEntity> listProductsByCategory(String category) {
        return produtoRepository.findByCategoryAndActiveTrue(category);
    }

    public List<ProdutoEntity> listAvailableProducts() {
        return produtoRepository.findProductAvailable();
    }

    public List<ProdutoEntity> searchByRestaurantAndCategory(Long restaurantId, String category) {
        return produtoRepository.findByRestaurantAndCategory(restaurantId, category);
    }

    // UPDATE
    @Transactional
    public ProdutoEntity updateProduct(Long id, ProdutoEntity updatedProduct) {
        ProdutoEntity existingProduct = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
        
        if (updatedProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O preço do produto deve ser maior que zero. ");
        }
        
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setAvailable(updatedProduct.getAvailable());
        
        return produtoRepository.save(existingProduct);
    }

    // DELETE
    @Transactional
    public void deactivateProduct(Long id) {
        ProdutoEntity product = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID:: " + id));
        
        product.setActive(false);
        produtoRepository.save(product);
    }

    // ACTIVATE
    @Transactional
    public void activateProduct(Long id) {
        ProdutoEntity product = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID:: " + id));
        
        product.setActive(true);
        produtoRepository.save(product);
    }

    @Transactional
    public void changeAvailability(Long id, Boolean available) {
        ProdutoEntity product = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID:: " + id));
        
        product.setAvailable(available);
        produtoRepository.save(product);
    }
}