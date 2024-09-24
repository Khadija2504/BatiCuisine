package com.batiCuisine.repository.imp;

import com.batiCuisine.repository.interf.DevisInterface;
import com.batiCuisine.model.Devis;
import com.batiCuisine.util.JdcbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DevisRepository implements DevisInterface {
    @Override
    public void createDevis(Devis devis) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String sql = "INSERT INTO devis (montant_estime, date_emission, date_validite, projet_id, accepte) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, devis.getMontant_estime());
            stmt.setDate(2, devis.getDate_emission());
            stmt.setDate(3, devis.getDate_validite());
            stmt.setInt(4, devis.getProject_id());
            stmt.setBoolean(5, devis.isAccepte());
            stmt.executeUpdate();
        }
    }
    @Override
    public Devis getDevisByProjectId(int project_id) throws SQLException {
        Connection connection = JdcbConnection.getConnection();
        String sql = "SELECT * FROM devis WHERE projet_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, project_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Devis devis = new Devis(
                    rs.getInt("id"),
                    rs.getDouble("montant_estime"),
                    rs.getDate("date_emission"),
                    rs.getDate("date_validite"),
                    rs.getBoolean("accepte"),
                    rs.getInt("projet_id")
                );
                return devis;
            }
        }
        return null;
    }

}
