package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Project;

import java.sql.SQLException;

public interface ProjetInterface {
    int addProject(Project project) throws SQLException;
}
