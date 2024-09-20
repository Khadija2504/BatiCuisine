package com.batiCuisine.model;

import com.batiCuisine.enums.TypeComposant;

public class Labor extends Composant {
    private double tauxHoraire;
    private double heuresTravail;
    private double productiviteOuvrier;

    public Labor(String nom, TypeComposant typeComposant, double tauxTva, int projetId, double tauxHoraire, double heuresTravail, double productiviteOuvrier) {
        super(nom, typeComposant, tauxTva, projetId);
        this.tauxHoraire = tauxHoraire;
        this.heuresTravail = heuresTravail;
        this.productiviteOuvrier = productiviteOuvrier;
    }

    public double getTauxHoraire() { return tauxHoraire; }
    public void setTauxHoraire(double tauxHoraire) { this.tauxHoraire = tauxHoraire; }

    public double getHeuresTravail() { return heuresTravail; }
    public void setHeuresTravail(double heuresTravail) { this.heuresTravail = heuresTravail; }

    public double getProductiviteOuvrier() { return productiviteOuvrier; }
    public void setProductiviteOuvrier(double productiviteOuvrier) { this.productiviteOuvrier = productiviteOuvrier; }
}
