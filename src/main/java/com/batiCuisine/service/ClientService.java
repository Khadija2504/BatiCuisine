package com.batiCuisine.service;

import com.batiCuisine.model.Client;
import com.batiCuisine.repository.imp.ClientRepository;

import java.sql.SQLException;
import java.util.Optional;

public class ClientService {
    private final ClientRepository clientRepository = new ClientRepository();

    public int addClient(String nom, String adresse, String telephone, boolean est_professionnel, double remise) throws SQLException {
        Client client = new Client(0, nom, adresse, telephone, est_professionnel, remise);
        return clientRepository.addClient(client);
    }

    public Optional<Client> searchClientByName(String name) throws SQLException {
        return clientRepository.findClientByName(name);
    }
}

