package com.batiCuisine.service;

import com.batiCuisine.model.Labor;
import com.batiCuisine.model.Material;
import com.batiCuisine.repository.imp.ComposantRepository;

import java.sql.SQLException;

public class ComposantService {
    private ComposantRepository repository = new ComposantRepository();

    public void createMateriaux(Material materiaux) throws SQLException {
        repository.save(materiaux);
    }

    public void createMainDoeuvre(Labor mainDoeuvre) throws SQLException {
        repository.save(mainDoeuvre);
    }
}
