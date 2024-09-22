package com.batiCuisine.service;

import com.batiCuisine.model.Labor;
import com.batiCuisine.model.Material;
import com.batiCuisine.repository.imp.ComposantRepository;

import java.sql.SQLException;
import java.util.List;

public class ComposantService {
    private ComposantRepository repository = new ComposantRepository();

    public void createMateriaux(Material materiaux) throws SQLException {
        repository.save(materiaux);
    }

    public void createMainDoeuvre(Labor mainDoeuvre) throws SQLException {
        repository.save(mainDoeuvre);
    }

    public List<Material> getMateriauxByProject(int projetId) {
        try {
            return repository.getMateriauxByProject(projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des matériaux: " + e.getMessage());
            return null;
        }
    }

    public List<Labor> getMainDoeuvreByProject(int projetId) {
        try {
            return repository.getMainDoeuvreByProject(projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la main-d'œuvre: " + e.getMessage());
            return null;
        }
    }

    public void updateTauxTVA(int composantId, double tauxTVA) throws SQLException {
        repository.updateTauxTVA(composantId, tauxTVA);
    }
}
