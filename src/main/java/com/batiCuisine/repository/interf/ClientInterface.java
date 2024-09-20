package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Client;

import java.sql.SQLException;

public interface ClientInterface {
    int addClient(Client client) throws SQLException;
}
