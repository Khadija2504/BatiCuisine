package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Composant;
import com.batiCuisine.model.Devis;

import java.sql.SQLException;

public interface DevisInterface {
    void createDevis(Devis devis) throws SQLException;
    Devis getDevisByProjectId(int projectId) throws SQLException;

}
