package com.batiCuisine.model;

import com.batiCuisine.enums.TypeComposant;

public class Material extends Composant {
    private double coutTransport;
    private double coefficientQualite;
    private double coutUnitaire;
    private double quantite;

    public Material(int id, String nom, TypeComposant typeComposant, double tauxTva, int projetId, double coutTransport, double coefficientQualite, double coutUnitaire, double quantite) {
        super(id, nom, typeComposant, tauxTva, projetId);
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
    }

    public double getCoutTransport() { return coutTransport; }
    public void setCoutTransport(double coutTransport) { this.coutTransport = coutTransport; }

    public double getCoefficientQualite() { return coefficientQualite; }
    public void setCoefficientQualite(double coefficientQualite) { this.coefficientQualite = coefficientQualite; }

    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(double coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
