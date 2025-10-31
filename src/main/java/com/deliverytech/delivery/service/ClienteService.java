package com.deliverytech.delivery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.ClienteEntity;
import com.deliverytech.delivery.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //CREATE
    @Transactional
    public ClienteEntity registerClient(ClienteEntity cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um usuário com este E-Mail: " + cliente.getEmail());
        }

        if (!cliente.getEmail().contains("@")) {
            throw new RuntimeException("E-Mail inválido: " + cliente.getEmail());
        }

        return clienteRepository.save(cliente);
    }

    //READ
    public Optional<ClienteEntity> searchForId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<ClienteEntity> searchForEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<ClienteEntity> listActiveClients() {
        return clienteRepository.findByActiveTrue();
    }

    public List<ClienteEntity> searchForName(String name) {
        return clienteRepository.findByNameContainingIgnoreCase(name);
    }

    //UPDATED
    @Transactional
    public ClienteEntity updateClient(Long id, ClienteEntity updatedClient) {
        ClienteEntity existingClient = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        if (!existingClient.getEmail().equals(updatedClient.getEmail())
                && clienteRepository.existsByEmail(updatedClient.getEmail())) {
            throw new RuntimeException("Já existe um cliente cadastrado com o E-Mail: " + updatedClient.getEmail());
        }

        existingClient.setName(updatedClient.getName());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setPhone(updatedClient.getPhone());
        existingClient.setAddress(updatedClient.getAddress());

        return clienteRepository.save(existingClient);
    }

    //DELETED
    @Transactional
    public void deletedClient(Long id) {
        ClienteEntity client = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo ID: " + id));
        client.setActive(false);
        clienteRepository.save(client);
    }

    //ACTIVE
    @Transactional
    public void activeClient(Long id) {
        ClienteEntity client = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo Id: " + id));
        client.setActive(true);
        clienteRepository.save(client);
    }

    public boolean activeClientExists(Long id) {
      return clienteRepository.findById(id)
          .map(ClienteEntity::getActive)
          .orElse(false);
  }
}
