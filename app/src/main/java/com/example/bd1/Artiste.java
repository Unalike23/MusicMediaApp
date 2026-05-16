package com.example.bd1;

public class Artiste {
    private int idA;
    private String nomA;
    private String description;

    public Artiste(int idA, String nomA, String description) {
        this.idA = idA;
        this.nomA = nomA;
        this.description = description;
    }

    public int getIdA() { return idA; }
    public String getNomA() { return nomA; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return nomA;
    }
}
