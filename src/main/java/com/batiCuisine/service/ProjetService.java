package com.batiCuisine.service;

import com.batiCuisine.enums.EtatProjet;
import com.batiCuisine.model.Client;
import com.batiCuisine.model.Project;
import com.batiCuisine.repository.imp.ProjetRepository;

import java.sql.SQLException;
import java.util.Optional;

public class ProjetService {
    EtatProjet etatProjet;
    ProjetRepository projetRepository = new ProjetRepository();
    public int addProject (String nom_projet, double surface, int client_id) throws SQLException {
        Project project = new Project(0, nom_projet, 0, 0, etatProjet, surface, client_id);
        return projetRepository.addProject(project);
    }

    public Project getProjectById(int projetId) throws SQLException {
        Optional<Project> project = projetRepository.findById(projetId);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new SQLException("Aucun projet trouvé avec l'ID " + projetId);
        }
    }

    public void updateProjectCost(int projetId, double coutTotal, double margeBeneficiaire) throws SQLException {
        projetRepository.updateCoutTotal(projetId, coutTotal, margeBeneficiaire);
    }
}
