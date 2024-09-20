package com.batiCuisine.model;

public class Client {
    private int id;
    private String nom;
    private String adresse;
    private String telephone;
    private boolean est_professionnel;
    private double remise;
    
    public Client(int id, String nom, String adresse, String telephone, boolean est_professionnel, double remise) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.est_professionnel = est_professionnel;
        this.remise = remise;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAddress() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean getEst_professionnel() {
        return est_professionnel;
    }

    public void setEst_professionnel(boolean est_professionnel) {
        this.est_professionnel = est_professionnel;
    }
}
