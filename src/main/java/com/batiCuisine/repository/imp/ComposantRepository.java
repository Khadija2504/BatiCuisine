package com.batiCuisine.repository.imp;

import com.batiCuisine.enums.TypeComposant;
import com.batiCuisine.model.Composant;
import com.batiCuisine.model.Labor;
import com.batiCuisine.model.Material;
import com.batiCuisine.util.JdcbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComposantRepository {

    public void save(Composant composant) throws SQLException {
        if (composant instanceof Material) {
            saveMaterial((Material) composant);
        } else if (composant instanceof Labor) {
            saveLabor((Labor) composant);
        }
    }

    private void saveMaterial(Material materiaux) throws SQLException {
        TypeComposant material = TypeComposant.Materiel;
        Connection connection = JdcbConnection.getConnection();
        String sql = "INSERT INTO materiaux (nom, cout_unitaire, quantite, type_composant, projet_id, cout_transport, coefficient_qualite) " +
                "VALUES (?, ?, ?, ?::TypeComposant, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, materiaux.getNom());
            stmt.setDouble(2, materiaux.getCoutUnitaire());
            stmt.setDouble(3, materiaux.getQuantite());
            stmt.setString(4, material.name());
            stmt.setInt(5, materiaux.getProjetId());
            stmt.setDouble(6, materiaux.getCoutTransport());
            stmt.setDouble(7, materiaux.getCoefficientQualite());
            stmt.executeUpdate();
        }
    }

    public List<Material> getMateriauxByProject(int projetId) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM materiaux WHERE projet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projetId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Material material = new Material(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        TypeComposant.valueOf(rs.getString("type_composant")),
                        rs.getDouble("taux_tva"),
                        rs.getInt("projet_id"),
                        rs.getDouble("cout_transport"),
                        rs.getDouble("coefficient_qualite"),
                        rs.getDouble("cout_unitaire"),
                        rs.getDouble("quantite")
                );
                materials.add(material);
            }
        }
        return materials;
    }

    public List<Labor> getMainDoeuvreByProject(int projetId) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        List<Labor> labors = new ArrayList<>();
        String query = "SELECT * FROM main_doeuvre WHERE projet_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projetId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Labor labor = new Labor(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        TypeComposant.valueOf(rs.getString("type_composant")),
                        rs.getDouble("taux_tva"),
                        rs.getInt("projet_id"),
                        rs.getDouble("taux_horaire"),
                        rs.getDouble("heures_travail"),
                        rs.getDouble("productivite_ouvrier")
                );
                labors.add(labor);
            }
        }
        return labors;
    }

    private void saveLabor(Labor mainDoeuvre) throws SQLException {
        TypeComposant main_doeuvre = TypeComposant.Main_doeuvre;
        Connection connection = JdcbConnection.getConnection();
        String sql = "INSERT INTO main_doeuvre (nom, type_composant, projet_id, taux_horaire, heures_travail, productivite_ouvrier) " +
                "VALUES (?, ?::TypeComposant, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mainDoeuvre.getNom());
            stmt.setString(2, main_doeuvre.name());
            stmt.setInt(3, mainDoeuvre.getProjetId());
            stmt.setDouble(4, mainDoeuvre.getTauxHoraire());
            stmt.setDouble(5, mainDoeuvre.getHeuresTravail());
            stmt.setDouble(6, mainDoeuvre.getProductiviteOuvrier());
            stmt.executeUpdate();
        }
    }

    public void updateTauxTVA(int composantId, double tauxTVA) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String sql = "UPDATE composants SET taux_tva = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, tauxTVA);
            statement.setInt(2, composantId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Taux de TVA mis à jour pour le composant ID : " + composantId);
            } else {
                System.out.println("Aucun composant trouvé avec l'ID : " + composantId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la mise à jour du taux de TVA pour le composant avec l'ID : " + composantId, e);
        }
    }
}
