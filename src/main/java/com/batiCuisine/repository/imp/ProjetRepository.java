package com.batiCuisine.repository.imp;

import com.batiCuisine.model.Project;
import com.batiCuisine.repository.interf.ProjetInterface;
import com.batiCuisine.util.JdcbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjetRepository implements ProjetInterface {

    @Override
    public int addProject(Project project) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "INSERT INTO projets (nom_projet, surface, client_id) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, project.getNom_projet());
        preparedStatement.setDouble(2, project.getSurface());
        preparedStatement.setInt(3, project.getClient_id());
        preparedStatement.executeUpdate();
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0) {
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int projectId = rs.getInt(1);
                project.setId(projectId);
                return projectId;
            }
        }
        return -1;
    }

    public void updateProjectCost(int projetId, double coutTotal) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "UPDATE projets SET cout_total = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, coutTotal);
            stmt.setInt(2, projetId);
            stmt.executeUpdate();
        }
    }
}
