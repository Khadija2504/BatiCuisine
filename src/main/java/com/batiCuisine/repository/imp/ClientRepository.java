package com.batiCuisine.repository.imp;

import com.batiCuisine.model.Client;
import com.batiCuisine.repository.interf.ClientInterface;
import com.batiCuisine.util.JdcbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientRepository implements ClientInterface {

    @Override
    public int addClient(Client client) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "INSERT INTO clients (nom, adresse, telephone, est_professionnel, remise) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, client.getNom());
        preparedStatement.setString(2, client.getAddress());
        preparedStatement.setString(3, client.getTelephone());
        preparedStatement.setBoolean(4, client.getEst_professionnel());
        preparedStatement.setDouble(5, client.getRemise());
        preparedStatement.executeUpdate();
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0) {
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int clientId = rs.getInt(1);
                client.setId(clientId);
                return clientId;
            }
        }
        return -1;
    }

    public Optional<Client> findClientByName(String name) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String sql = "SELECT * FROM clients WHERE nom = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("adresse"),
                        resultSet.getString("telephone"),
                        resultSet.getBoolean("est_professionnel"),
                        resultSet.getDouble("remise")
                );
                return Optional.of(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du client avec le nom : " + name, e);
        }

        return Optional.empty();
    }
}
