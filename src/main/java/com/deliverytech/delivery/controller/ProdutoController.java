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

import com.deliverytech.delivery.entity.ProdutoEntity;
import com.deliverytech.delivery.service.ProdutoService;

@RestController
@RequestMapping("api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/restaurante/{restaurantId}")
    public ResponseEntity<?> criarProduto(@PathVariable Long restaurantId, @RequestBody ProdutoEntity produto) {
        try {
            ProdutoEntity produtoSalvo = produtoService.registerProduct(restaurantId, produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoEntity>> listarProdutosDisponiveis() {
        List<ProdutoEntity> produtos = produtoService.listAvailableProducts();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable Long id) {
        Optional<ProdutoEntity> produto = produtoService.searchById(id);
        
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Produto não encontrado com ID: " + id);
        }
    }

    @GetMapping("/restaurante/{restaurantId}")
    public ResponseEntity<List<ProdutoEntity>> listarProdutosPorRestaurante(@PathVariable Long restaurantId) {
        List<ProdutoEntity> produtos = produtoService.listProductsByRestaurant(restaurantId);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/restaurante/{restaurantId}/disponiveis")
    public ResponseEntity<List<ProdutoEntity>> listarProdutosDisponiveisPorRestaurante(@PathVariable Long restaurantId) {
        List<ProdutoEntity> produtos = produtoService.listAvailableProductsByRestaurant(restaurantId);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoEntity>> listarProdutosPorCategoria(@PathVariable String categoria) {
        List<ProdutoEntity> produtos = produtoService.listProductsByCategory(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/restaurante/{restaurantId}/categoria/{categoria}")
    public ResponseEntity<List<ProdutoEntity>> buscarPorRestauranteECategoria(
            @PathVariable Long restaurantId, 
            @PathVariable String categoria) {
        List<ProdutoEntity> produtos = produtoService.searchByRestaurantAndCategory(restaurantId, categoria);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoEntity produtoAtualizado) {
        try {
            ProdutoEntity produto = produtoService.updateProduct(id, produtoAtualizado);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desativarProduto(@PathVariable Long id) {
        try {
            produtoService.deactivateProduct(id);
            return ResponseEntity.ok("Produto desativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<?> ativarProduto(@PathVariable Long id) {
        try {
            produtoService.activateProduct(id);
            return ResponseEntity.ok("Produto ativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<?> alterarDisponibilidade(@PathVariable Long id, @RequestParam Boolean disponivel) {
        try {
            produtoService.changeAvailability(id, disponivel);
            return ResponseEntity.ok("Disponibilidade do produto atualizada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}