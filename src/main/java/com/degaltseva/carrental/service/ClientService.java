package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.Client;
import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.repository.ClientRepository;
import com.degaltseva.carrental.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientService {

    private final ClientRepository clientRepository = new ClientRepository();
    private final UserRepository userRepository = new UserRepository();

    public List<Client> findAll() {
        List<Client> clients = clientRepository.findAll();
        enrichAll(clients);
        return clients;
    }

    public Optional<Client> findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        client.ifPresent(this::enrich);
        return client;
    }

    public Optional<Client> findByUserId(Long userId) {
        Optional<Client> client = clientRepository.findByUserId(userId);
        client.ifPresent(this::enrich);
        return client;
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Client client) {
        return clientRepository.update(client);
    }

    public void delete(Long id) {
        clientRepository.delete(id);
    }

    private void enrichAll(List<Client> clients) {
        if (clients.isEmpty()) return;

        Map<Long, User> users = userRepository.findAll().stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        for (Client client : clients) {
            User user = users.get(client.getUserId());
            if (user != null) {
                client.setUsername(user.getUsername());
            }
        }
    }

    private void enrich(Client client) {
        userRepository.findById(client.getUserId())
                .ifPresent(u -> client.setUsername(u.getUsername()));
    }
}
