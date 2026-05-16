package com.example.bd1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class ProduitDAO {
    private BdSQLiteOpenHelper dbHelper;

    private Context context;

    public ProduitDAO(Context context) {
        this.context = context;
        dbHelper = new BdSQLiteOpenHelper(context, "MusicDB", null, 3);
    }

    public ArrayList<Categorie> getCategories() {
        ArrayList<Categorie> list = new ArrayList<>();
        list.add(new Categorie(0, context.getString(R.string.all_categories)));
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORIE", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Categorie(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Genre> getGenres() {
        ArrayList<Genre> list = new ArrayList<>();
        list.add(new Genre(0, context.getString(R.string.all_genres)));
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GENRE", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Genre(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Produit> getProduits(int idC, int idG, String sortOrder, String searchTerm) {
        ArrayList<Produit> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT P.idP, P.nomP, P.prix, C.nomC, P.imageName, P.favoris FROM PRODUIT P " +
                "JOIN CATEGORIE C ON P.idC = C.idC ";
        
        ArrayList<String> args = new ArrayList<>();
        String where = "";

        if (idC > 0) {
            where += "P.idC = ? ";
            args.add(String.valueOf(idC));
        }

        if (idG > 0) {
            if (!where.isEmpty()) where += "AND ";
            where += "P.idP IN (SELECT idP FROM PRODUIT_GENRE WHERE idG = ?) ";
            args.add(String.valueOf(idG));
        }

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            if (!where.isEmpty()) where += "AND ";
            where += "P.nomP LIKE ? ";
            args.add("%" + searchTerm + "%");
        }

        if (!where.isEmpty()) {
            query += "WHERE " + where;
        }

        if (sortOrder.equals("ASC")) {
            query += "ORDER BY P.prix ASC";
        } else {
            query += "ORDER BY P.prix DESC";
        }

        Cursor cursor = db.rawQuery(query, args.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                Produit p = new Produit(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4));
                p.setFavoris(cursor.getInt(5) == 1);
                
                // Fetch artists
                Cursor cA = db.rawQuery("SELECT nomA FROM ARTISTE A JOIN PRODUIT_ARTISTE PA ON A.idA = PA.idA WHERE PA.idP = ?", new String[]{String.valueOf(p.getIdP())});
                if (cA.moveToFirst()) {
                    do { p.addArtiste(cA.getString(0)); } while (cA.moveToNext());
                }
                cA.close();

                // Fetch genres
                Cursor cG = db.rawQuery("SELECT nomG FROM GENRE G JOIN PRODUIT_GENRE PG ON G.idG = PG.idG WHERE PG.idP = ?", new String[]{String.valueOf(p.getIdP())});
                if (cG.moveToFirst()) {
                    do { p.addGenre(cG.getString(0)); } while (cG.moveToNext());
                }
                cG.close();

                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Artiste> getArtistesByProduit(int idP) {
        ArrayList<Artiste> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT A.idA, A.nomA, A.description FROM ARTISTE A " +
                "JOIN PRODUIT_ARTISTE PA ON A.idA = PA.idA " +
                "WHERE PA.idP = ?", new String[]{String.valueOf(idP)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Artiste(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateFavoris(int idP, boolean isFavori) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("favoris", isFavori ? 1 : 0);
        db.update("PRODUIT", values, "idP = ?", new String[]{String.valueOf(idP)});
    }

    public ArrayList<Produit> getFavoris() {
        ArrayList<Produit> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT P.idP, P.nomP, P.prix, C.nomC, P.imageName, P.favoris FROM PRODUIT P " +
                "JOIN CATEGORIE C ON P.idC = C.idC WHERE P.favoris = 1";
        android.database.Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Produit p = new Produit(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4));
                p.setFavoris(true);
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
