package com.batiCuisine.service;

import com.batiCuisine.model.Client;
import com.batiCuisine.repository.imp.ClientRepository;

import java.sql.SQLException;

public class ClientService {
    ClientRepository clientRepository = new ClientRepository();

    public int addClient(String nom, String adresse, String telephone, boolean est_professionnel, double remise) throws SQLException {
        Client client = new Client(0, nom, adresse, telephone, est_professionnel, remise);
        return clientRepository.addClient(client);
    }
}

