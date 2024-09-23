package com.batiCuisine.service;

import com.batiCuisine.model.Devis;
import com.batiCuisine.repository.imp.DevisRepository;

import java.sql.Date;
import java.sql.SQLException;

public class DevisService {
    private final DevisRepository devisRepository = new DevisRepository();
    public void createDevis(int projetId, double calcCoutTotal, Date date_emission, Date date_validite, boolean validate) throws SQLException {
        Devis devis = new Devis(0, calcCoutTotal, date_emission, date_validite,validate,projetId);
        devisRepository.createDevis(devis);
    }
    public Devis getDevisByProjectId(int devisId) throws SQLException {
        return devisRepository.getDevisByProjectId(devisId);
    }
}
