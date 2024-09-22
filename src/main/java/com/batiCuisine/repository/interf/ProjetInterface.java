package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Project;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProjetInterface {
    int addProject(Project project) throws SQLException;
    void updateCoutTotal(int projetId, double coutTotal, double margeBeneficiaire) throws SQLException;
    Optional<Project> findById(int projetId) throws SQLException;
    List<Project> getAllProjects() throws SQLException;
    }
