package com.batiCuisine.service;

import com.batiCuisine.enums.EtatProjet;
import com.batiCuisine.model.Client;
import com.batiCuisine.model.Project;
import com.batiCuisine.repository.imp.ProjetRepository;

import java.sql.SQLException;

public class ProjetService {
    EtatProjet etatProjet;
    ProjetRepository projetRepository = new ProjetRepository();
    public int addProject (String nom_projet, double surface, int client_id) throws SQLException {
        Project project = new Project(0, nom_projet, 0, 0, etatProjet, surface, client_id);
        return projetRepository.addProject(project);
    }

    public void updateProjectCost(int projetId, double coutTotal) throws SQLException {
        projetRepository.updateProjectCost(projetId, coutTotal);
    }
}
