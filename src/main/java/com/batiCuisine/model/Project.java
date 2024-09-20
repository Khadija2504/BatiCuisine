package com.batiCuisine.model;

import com.batiCuisine.enums.EtatProjet;

public class Project {
    private int id;
    private String nom_projet;
    private double marge_beneficiaire;
    private double cout_total;
    private EtatProjet etat_projet;
    private double surface;
    private int client_id;

    public Project(int id, String nom_projet, double marge_beneficiaire, double cout_total, EtatProjet etat_projet, double surface, int client_id) {
        this.id = id;
        this.nom_projet = nom_projet;
        this.marge_beneficiaire = marge_beneficiaire;
        this.cout_total = cout_total;
        this.etat_projet = etat_projet;
        this.surface = surface;
        this.client_id = client_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_projet() {
        return nom_projet;
    }

    public void setNom_projet(String nom_projet) {
        this.nom_projet = nom_projet;
    }

    public double getMarge_beneficiaire() {
        return marge_beneficiaire;
    }

    public void setMarge_beneficiaire(double marge_beneficiaire) {
        this.marge_beneficiaire = marge_beneficiaire;
    }

    public double getCout_total() {
        return cout_total;
    }

    public void setCout_total(double cout_total) {
        this.cout_total = cout_total;
    }

    public EtatProjet getEtat_projet() {
        return etat_projet;
    }

    public void setEtat_projet(EtatProjet etat_projet) {
        this.etat_projet = etat_projet;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}
