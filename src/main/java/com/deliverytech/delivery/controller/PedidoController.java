package com.deliverytech.delivery.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.OrderItemDTO;
import com.deliverytech.delivery.entity.PedidoEntity;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.service.PedidoService;

@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            List<OrderItemDTO> orderItems = request.getItems().stream()
                .map(item -> {
                    OrderItemDTO orderItem = new OrderItemDTO();
                    orderItem.setProductId(item.getProductId());
                    orderItem.setQuantity(item.getQuantity());
                    return orderItem;
                })
                .toList();
            
            PedidoEntity order = pedidoService.createOrder(
                request.getClientId(), 
                request.getRestaurantId(), 
                request.getDeliveryAddress(), 
                orderItems
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoEntity>> getAllOrders() {
        List<PedidoEntity> orders = pedidoService.listOrdersByStatus(StatusPedido.PENDING);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<PedidoEntity> order = pedidoService.searchById(id);
        
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Pedido não encontrado com o ID " + id);
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PedidoEntity>> getOrdersByClient(@PathVariable Long clientId) {
        List<PedidoEntity> orders = pedidoService.listOrdersByClient(clientId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<PedidoEntity>> getActiveOrdersByClient(@PathVariable Long clientId) {
        List<PedidoEntity> orders = pedidoService.listActiveOrdersByClient(clientId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoEntity>> getOrdersByStatus(@PathVariable StatusPedido status) {
        List<PedidoEntity> orders = pedidoService.listOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        try {
            PedidoEntity order = pedidoService.updateStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            PedidoEntity order = pedidoService.cancelOrder(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/estimated-time")
    public ResponseEntity<?> getEstimatedTime(@PathVariable Long id) {
        try {
            Integer estimatedTime = pedidoService.calculateEstimatedTime(id);
            return ResponseEntity.ok("Tempo estimado de entrega: " + estimatedTime + " minutos");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

class CreateOrderRequest {
    private Long clientId;
    private Long restaurantId;
    private String deliveryAddress;
    private List<OrderItemRequest> items;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}

class OrderItemRequest {
    private Long productId;
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
