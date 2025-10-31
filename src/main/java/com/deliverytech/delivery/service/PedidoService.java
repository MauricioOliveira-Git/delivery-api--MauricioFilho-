package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.ClienteEntity;
import com.deliverytech.delivery.entity.ItemPedidoEntity;
import com.deliverytech.delivery.entity.PedidoEntity;
import com.deliverytech.delivery.entity.ProdutoEntity;
import com.deliverytech.delivery.entity.RestauranteEntity;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    // CREATE
    @Transactional
    public PedidoEntity createOrder(Long clientId, Long restaurantId, String deliveryAddress, List<OrderItem> items) {
        if (!clienteService.activeClientExists(clientId)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + clientId);
        }

        RestauranteEntity restaurant = restauranteService.searchById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID " + restaurantId));
        
        if (!restaurant.getActive()) {
            throw new RuntimeException("Restaurante está Inativo");
        }

        ClienteEntity client = clienteService.searchForId(clientId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clientId));

        PedidoEntity order = new PedidoEntity(client, restaurant, deliveryAddress);

        BigDecimal orderTotal = BigDecimal.ZERO;
        
        for (OrderItem item : items) {
            ProdutoEntity product = produtoService.searchById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + item.getProductId()));
            
            if (!product.getAvailable() || !product.getActive()) {
                throw new RuntimeException("Produto não Disponivel " + product.getName());
            }

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("A quantidade deve ser maior que zero para o produto: " + product.getName());
            }

            if (!product.getRestaurant().getId().equals(restaurantId)) {
                throw new RuntimeException("O produto não pertence ao restaurante selecionado.: " + product.getName());
            }

            ItemPedidoEntity orderItem = new ItemPedidoEntity(order, product, item.getQuantity());
            order.getItems().add(orderItem);
            
            orderTotal = orderTotal.add(orderItem.getTotalPrice());
        }

        if (order.getItems().isEmpty()) {
            throw new RuntimeException("O pedido deve conter pelo menos um item.");
        }

        order.setTotalAmount(orderTotal);
        return pedidoRepository.save(order);
    }

    // READ
    public Optional<PedidoEntity> searchById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<PedidoEntity> listOrdersByClient(Long clientId) {
        return pedidoRepository.findByClientId(clientId);
    }

    public List<PedidoEntity> listOrdersByStatus(StatusPedido status) {
        return pedidoRepository.findbyStatus(status);
    }

    public List<PedidoEntity> listActiveOrdersByClient(Long clientId) {
        List<PedidoEntity> orders = pedidoRepository.findByClientId(clientId);
        return orders.stream()
            .filter(order -> order.getStatus() != StatusPedido.DELIVERED && order.getStatus() != StatusPedido.CANCELED)
            .toList();
    }

    // UPDATE
    @Transactional
    public PedidoEntity updateStatus(Long orderId, StatusPedido newStatus) {
        PedidoEntity order = pedidoRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + orderId));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        return pedidoRepository.save(order);
    }

    private void validateStatusTransition(StatusPedido currentStatus, StatusPedido newStatus) {
        if (currentStatus == StatusPedido.CANCELED) {
            throw new RuntimeException("Não é possível alterar o status de um pedido cancelado.");
        }
        
        if (currentStatus == StatusPedido.DELIVERED) {
            throw new RuntimeException("Não é possível alterar o status de um pedido entregue.");
        }
    }

    @Transactional
    public PedidoEntity cancelOrder(Long orderId) {
        PedidoEntity order = pedidoRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + orderId));

        if (order.getStatus() != StatusPedido.PENDING && order.getStatus() != StatusPedido.CONFIRMED) {
            throw new RuntimeException("O pedido não pode ser cancelado no estado atual: " + order.getStatus());
        }

        order.setStatus(StatusPedido.CANCELED);
        return pedidoRepository.save(order);
    }

    public Integer calculateEstimatedTime(Long orderId) {
        PedidoEntity order = pedidoRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + orderId));

        return order.getRestaurant().getDeliveryTime() + 10;
    }
}

class OrderItem {
    private Long productId;
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}