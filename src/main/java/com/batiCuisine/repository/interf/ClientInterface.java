package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Client;

import java.sql.SQLException;
import java.util.Optional;

public interface ClientInterface {
    int addClient(Client client) throws SQLException;
    Optional<Client> findClientByName(String name) throws SQLException;
}
