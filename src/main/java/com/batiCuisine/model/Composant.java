package com.batiCuisine.model;

import com.batiCuisine.enums.TypeComposant;

public class Composant {
    private int id;
    private String nom;
    private TypeComposant typeComposant;
    private double tauxTva;
    private int projetId;

    public Composant(String nom, TypeComposant typeComposant, double tauxTva, int projetId) {
        this.nom = nom;
        this.typeComposant = typeComposant;
        this.tauxTva = tauxTva;
        this.projetId = projetId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeComposant getTypeComposant() { return typeComposant; }
    public void setTypeComposant(TypeComposant typeComposant) { this.typeComposant = typeComposant; }

    public double getTauxTva() { return tauxTva; }
    public void setTauxTva(double tauxTva) { this.tauxTva = tauxTva; }

    public int getProjetId() { return projetId; }
    public void setProjetId(int projetId) { this.projetId = projetId; }

}
