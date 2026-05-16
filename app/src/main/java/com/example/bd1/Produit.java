package com.example.bd1;

import java.util.ArrayList;

public class Produit {
    private int idP;
    private String nomP;
    private double prix;
    private String nomCategorie;
    private String imageName;
    private ArrayList<String> artistes;
    private ArrayList<String> genres;
    private boolean isFavori;

    public Produit(int idP, String nomP, double prix, String nomCategorie, String imageName) {
        this.idP = idP;
        this.nomP = nomP;
        this.prix = prix;
        this.nomCategorie = nomCategorie;
        this.imageName = imageName;
        this.artistes = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public int getIdP() { return idP; }
    public String getNomP() { return nomP; }
    public double getPrix() { return prix; }
    public String getNomCategorie() { return nomCategorie; }
    public String getImageName() { return imageName; }
    public ArrayList<String> getArtistes() { return artistes; }
    public ArrayList<String> getGenres() { return genres; }
    public boolean isFavori() { return isFavori; }
    public void setFavoris(boolean favori) { isFavori = favori; }

    public void addArtiste(String nomA) { artistes.add(nomA); }
    public void addGenre(String nomG) { genres.add(nomG); }
}
