package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Composant;
import com.batiCuisine.model.Devis;

import java.sql.SQLException;
import java.util.Map;

public interface DevisInterface {
    void createDevis(Devis devis) throws SQLException;
    Map<Integer, Devis> getDevisByProjectId(int projectId) throws SQLException;

}
