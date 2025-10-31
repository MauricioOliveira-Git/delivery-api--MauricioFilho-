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

import com.deliverytech.delivery.entity.ClienteEntity;
import com.deliverytech.delivery.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClienteEntity cliente) {
        try {
            ClienteEntity savedClient = clienteService.registerClient(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<ClienteEntity>> getAllActiveClients() {
        List<ClienteEntity> clients = clienteService.listActiveClients();
        return ResponseEntity.ok(clients);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        Optional<ClienteEntity> client = clienteService.searchForId(id);
        
        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cliente não encontrado com o Id: " + id);
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> getClientByEmail(@PathVariable String email) {
        Optional<ClienteEntity> client = clienteService.searchForEmail(email);
        
        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cliente não encontrado com o E-Mail: " + email);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<ClienteEntity>> searchClientsByName(@RequestParam String name) {
        List<ClienteEntity> clients = clienteService.searchForName(name);
        return ResponseEntity.ok(clients);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClienteEntity updatedClient) {
        try {
            ClienteEntity client = clienteService.updateClient(id, updatedClient);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateClient(@PathVariable Long id) {
        try {
            clienteService.deletedClient(id);
            return ResponseEntity.ok("Cliente desativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateClient(@PathVariable Long id) {
        try {
            clienteService.activeClient(id);
            return ResponseEntity.ok("Cliente ativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}