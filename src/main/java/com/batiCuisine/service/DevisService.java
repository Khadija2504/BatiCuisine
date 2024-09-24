package com.batiCuisine.service;

import com.batiCuisine.model.Devis;
import com.batiCuisine.repository.imp.DevisRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DevisService {
    private final DevisRepository devisRepository = new DevisRepository();
    public void createDevis(int projetId, double calcCoutTotal, Date date_emission, Date date_validite, boolean validate) throws SQLException {
        Devis devis = new Devis(0, calcCoutTotal, date_emission, date_validite,validate,projetId);
        devisRepository.createDevis(devis);
    }
    public Map<Integer, Devis> getDevisByProjectId(int projectId) {
        try {
            return devisRepository.getDevisByProjectId(projectId);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des devis : " + e.getMessage());
            return new HashMap<>();
        }
    }

}
