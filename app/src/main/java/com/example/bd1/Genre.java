package com.example.bd1;

public class Genre {
    private int idG;
    private String nomG;

    public Genre(int idG, String nomG) {
        this.idG = idG;
        this.nomG = nomG;
    }

    public int getIdG() { return idG; }
    public String getNomG() { return nomG; }

    @Override
    public String toString() {
        return nomG;
    }
}
