package com.alvarado.chadecat_app;

public class Missatges {
    private String nom;
    private String missatge;


    public Missatges(){

    }

    public Missatges(String nom, String missatge){
        this.nom = nom;
        this.missatge = missatge;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMissatge() {
        return missatge;
    }

    public void setMissatge(String missatge) {
        this.missatge = missatge;
    }
}