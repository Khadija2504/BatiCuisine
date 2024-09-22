package com.batiCuisine.repository.imp;

import com.batiCuisine.enums.EtatProjet;
import com.batiCuisine.model.Client;
import com.batiCuisine.model.Project;
import com.batiCuisine.repository.interf.ProjetInterface;
import com.batiCuisine.util.JdcbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjetRepository implements ProjetInterface {

    @Override
    public int addProject(Project project) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "INSERT INTO projets (nom_projet, surface, client_id) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, project.getNom_projet());
        preparedStatement.setDouble(2, project.getSurface());
        preparedStatement.setInt(3, project.getClient_id());
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

    @Override
    public void updateCoutTotal(int projetId, double coutTotal, double margeBeneficiaire) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "UPDATE projets SET cout_total = ?, marge_beneficiaire = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, coutTotal);
            stmt.setDouble(2, margeBeneficiaire);
            stmt.setInt(3, projetId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Project> findById(int projetId) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String query = "SELECT p.*, c.*" +
                "FROM projets p " +
                "JOIN clients c ON p.client_id = c.id " +
                "WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projetId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("est_professionnel"),
                        rs.getDouble("remise")
                );
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("nom_projet"),
                        rs.getDouble("marge_beneficiaire"),
                        rs.getDouble("cout_total"),
                        EtatProjet.valueOf(rs.getString("etat_projet")),
                        rs.getDouble("surface"),
                        rs.getInt("client_id"),
                        client
                );
                return Optional.of(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du projet avec l'ID : " + projetId, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Project> getAllProjects() throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projets";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("nom_projet"),
                        rs.getDouble("marge_beneficiaire"),
                        rs.getDouble("cout_total"),
                        EtatProjet.valueOf(rs.getString("etat_projet")),
                        rs.getDouble("surface"),
                        rs.getInt("client_id")
                );
                projects.add(project);
            }
        }
        return projects;
    }

}
