package com.batiCuisine.model;

import java.sql.Date;

public class Devis {
    private int id;
    private double montant_estime;
    private Date date_emission;
    private Date date_validite;
    private boolean accepte;
    private int project_id;
     public Devis(int id, double montant_estime, Date date_emission, Date date_validite, boolean accepte, int project_id) {
         this.id = id;
         this.montant_estime = montant_estime;
         this.date_emission = date_emission;
         this.date_validite = date_validite;
         this.accepte = accepte;
         this.project_id = project_id;
     }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant_estime() {
        return montant_estime;
    }

    public void setMontant_estime(double montant_estime) {
        this.montant_estime = montant_estime;
    }

    public Date getDate_emission() {
        return date_emission;
    }

    public void setDate_emission(Date date_emission) {
        this.date_emission = date_emission;
    }

    public java.sql.Date getDate_validite() {
        return date_validite;
    }

    public void setDate_validite(Date date_validite) {
        this.date_validite = date_validite;
    }

    public boolean isAccepte() {
        return accepte;
    }

    public void setAccepte(boolean accepte) {
        this.accepte = accepte;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
