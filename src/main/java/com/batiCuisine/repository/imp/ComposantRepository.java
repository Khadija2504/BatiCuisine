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

}
