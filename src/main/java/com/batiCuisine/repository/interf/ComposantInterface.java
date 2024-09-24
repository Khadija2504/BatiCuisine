package com.batiCuisine.repository.interf;

import com.batiCuisine.model.Composant;
import com.batiCuisine.model.Labor;
import com.batiCuisine.model.Material;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ComposantInterface {
    void save(Composant composant) throws SQLException;
    void saveMaterial(Material materiaux) throws SQLException;
    void saveLabor(Labor mainDoeuvre) throws SQLException;
    List<Material> getMateriauxByProject(int projetId) throws SQLException;
    List<Labor> getMainDoeuvreByProject(int projetId) throws SQLException;
    Map<Integer, Composant> getAllComposants(int projetId) throws SQLException;
    void updateTauxTVA(int composantId, double tauxTVA) throws SQLException;


}
