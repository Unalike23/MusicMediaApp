package com.example.bd1;

public class Categorie {
    private int idC;
    private String nomC;

    public Categorie(int idC, String nomC) {
        this.idC = idC;
        this.nomC = nomC;
    }

    public int getIdC() { return idC; }
    public String getNomC() { return nomC; }
    
    @Override
    public String toString() {
        return nomC;
    }
}
