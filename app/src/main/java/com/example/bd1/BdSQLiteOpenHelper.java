package com.example.bd1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BdSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_CATEGORIE = "CATEGORIE";
    private static final String TABLE_GENRE = "GENRE";
    private static final String TABLE_ARTISTE = "ARTISTE";
    private static final String TABLE_PRODUIT = "PRODUIT";
    private static final String TABLE_PRODUIT_ARTISTE = "PRODUIT_ARTISTE";
    private static final String TABLE_PRODUIT_GENRE = "PRODUIT_GENRE";

    public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIE + " (idC INTEGER PRIMARY KEY AUTOINCREMENT, nomC TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_GENRE + " (idG INTEGER PRIMARY KEY AUTOINCREMENT, nomG TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_ARTISTE + " (idA INTEGER PRIMARY KEY AUTOINCREMENT, nomA TEXT, description TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_PRODUIT + " (idP INTEGER PRIMARY KEY AUTOINCREMENT, nomP TEXT, prix REAL, idC INTEGER, imageName TEXT, favoris INTEGER DEFAULT 0, FOREIGN KEY(idC) REFERENCES CATEGORIE(idC));");
        db.execSQL("CREATE TABLE " + TABLE_PRODUIT_ARTISTE + " (idP INTEGER, idA INTEGER, PRIMARY KEY(idP, idA), FOREIGN KEY(idP) REFERENCES PRODUIT(idP), FOREIGN KEY(idA) REFERENCES ARTISTE(idA));");
        db.execSQL("CREATE TABLE " + TABLE_PRODUIT_GENRE + " (idP INTEGER, idG INTEGER, PRIMARY KEY(idP, idG), FOREIGN KEY(idP) REFERENCES PRODUIT(idP), FOREIGN KEY(idG) REFERENCES GENRE(idG));");

        // Seed Categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIE + " (nomC) VALUES ('Vinyle'), ('CD'), ('Cassette');");

        // Seed Genres (using genres from SQL dump)
        db.execSQL("INSERT INTO " + TABLE_GENRE + " (nomG) VALUES ('Rock'), ('Rap'), ('Electro'), ('Hiphop'), ('Jazz'), ('Pop'), ('Metal'), ('Alternative'), ('Hyperpop'), ('Breakcore'), ('Jungle'), ('Soul'), ('Disco'), ('Ambient'), ('Indie');");

        // Seed Artists (using names from SQL dump)
        db.execSQL("INSERT INTO " + TABLE_ARTISTE + " (nomA, description) VALUES ('Daft Punk', 'French electronic music duo'), ('Pink Floyd', 'English rock band'), ('Kendrick Lamar', 'American rapper'), ('Miles Davis', 'American jazz trumpeter'), ('The Beatles', 'English rock band'), ('Michael Jackson', 'King of Pop'), ('Linkin Park', 'American rock band'), ('System of a Down', 'Armenian-American heavy metal band'), ('The Garden', 'American experimental rock band'), ('Joey Valence & Brae', 'American hip hop duo'), ('Deftones', 'American alternative metal band'), ('Machine Girl', 'American electronic music project'), ('Zola', 'French rapper'), ('Tame Impala', 'Australian psych-rock project'), ('Tyler, The Creator', 'American rapper and producer'), ('Björk', 'Icelandic singer-songwriter'), ('Magdalena Bay', 'American synth-pop duo'), ('MF DOOM', 'British-American rapper'), ('Playboi Carti', 'American rapper'), ('Nirvana', 'American grunge band'), ('Cynthoni', 'Electronic music artist'), ('Metallica', 'American heavy metal band'), ('JPEGMAFIA', 'American rapper'), ('Danny Brown', 'American rapper'), ('Volbeat', 'Danish rock band'), ('Werenoi', 'French rapper'), ('Jane Remover', 'American musician'), ('Puzzle', 'Experimental music project'), ('PinkPantheress', 'English singer');");

        // Seed Products with images (matching SQL dump logic)
        insertProduit(db, "Hybrid Theory", 30.99, 1, "prod_68cb2436b44d4", new String[]{"Linkin Park"}, new String[]{"Rock", "Metal", "Alternative"});
        insertProduit(db, "Toxicity", 20.00, 1, "toxicity", new String[]{"System of a Down"}, new String[]{"Metal", "Alternative"});
        insertProduit(db, "Kiss My Super Bowl Ring", 28.00, 1, "kmsbr", new String[]{"The Garden"}, new String[]{"Alternative", "Punk"});
        insertProduit(db, "WLFGRL", 28.99, 1, "wlfgrl", new String[]{"Machine Girl"}, new String[]{"Breakcore", "Electro", "Jungle"});
        insertProduit(db, "NO HANDS", 29.99, 2, "nohands", new String[]{"Joey Valence & Brae"}, new String[]{"Hip-hop", "Electro"});
        insertProduit(db, "Around the Fur", 31.90, 1, "aroundthefur", new String[]{"Deftones"}, new String[]{"Metal", "Alternative"});
        insertProduit(db, "Diamant du Bled", 26.99, 1, "diamantdubled", new String[]{"Zola"}, new String[]{"Rap", "Trap"});
        insertProduit(db, "Survie", 10.99, 2, "survie", new String[]{"Zola"}, new String[]{"Rap", "Trap"});
        insertProduit(db, "Currents", 39.99, 1, "currents", new String[]{"Tame Impala"}, new String[]{"Electro", "Indie"});
        insertProduit(db, "haha", 21.99, 1, "haha", new String[]{"The Garden"}, new String[]{"Alternative", "Electro"});
        insertProduit(db, "Flower Boy", 29.99, 1, "flowerboy", new String[]{"Tyler, The Creator"}, new String[]{"Rap", "Jazz", "Soul"});
        insertProduit(db, "Call Me If You Get Lost", 29.99, 1, "call_me_if_you_get_lost", new String[]{"Tyler, The Creator"}, new String[]{"Rap"});
        insertProduit(db, "IGOR", 24.99, 1, "igor", new String[]{"Tyler, The Creator"}, new String[]{"Rap", "Soul", "Electro"});
        insertProduit(db, "Debut", 17.00, 2, "debut", new String[]{"Björk"}, new String[]{"Electro", "Pop", "Jazz"});
        insertProduit(db, "Post", 29.99, 1, "post", new String[]{"Björk"}, new String[]{"Electro", "Alternative"});
        insertProduit(db, "Imaginal Disk", 16.99, 2, "imaginaldisk", new String[]{"Magdalena Bay"}, new String[]{"Pop", "Electro"});
        insertProduit(db, "Good Kid, M.A.A.D City", 35.99, 1, "gkmc", new String[]{"Kendrick Lamar"}, new String[]{"Rap", "Hiphop"});
        insertProduit(db, "To Pimp a Butterfly", 30.00, 1, "tpab", new String[]{"Kendrick Lamar"}, new String[]{"Rap", "Jazz", "Soul"});
        insertProduit(db, "DAMN.", 28.99, 1, "damn", new String[]{"Kendrick Lamar"}, new String[]{"Rap"});
        insertProduit(db, "MM..FOOD", 30.00, 1, "mmfood", new String[]{"MF DOOM"}, new String[]{"Rap", "Hiphop"});
        insertProduit(db, "Die Lit", 17.00, 2, "dielit", new String[]{"Playboi Carti"}, new String[]{"Rap", "Trap"});
        insertProduit(db, "Nevermind", 20.00, 1, "nevermind", new String[]{"Nirvana"}, new String[]{"Rock", "Alternative"});
        insertProduit(db, "In Utero", 34.99, 1, "in_utero", new String[]{"Nirvana"}, new String[]{"Rock", "Alternative"});
        insertProduit(db, "Cynthoni Vinyl", 33.99, 1, "cynthonivinyl", new String[]{"Cynthoni"}, new String[]{"Breakcore", "Electro"});
        insertProduit(db, "One Last Kiss", 34.99, 1, "onelastkiss", new String[]{"Björk"}, new String[]{"Pop", "Electro"});
        insertProduit(db, "Sincèrement", 13.50, 2, "sincerement", new String[]{"Hamza"}, new String[]{"Rap", "Soul"});
        insertProduit(db, "The Black Album", 37.99, 1, "blackalbum", new String[]{"Metallica"}, new String[]{"Metal"});
        insertProduit(db, "Chaser", 34.99, 1, "chaser", new String[]{"Joey Valence & Brae"}, new String[]{"Rap", "Electro"});
        insertProduit(db, "Meteora", 28.99, 1, "meteora", new String[]{"Linkin Park"}, new String[]{"Rock", "Metal", "Alternative"});
        insertProduit(db, "I LAY DOWN MY LIFE FOR YOU", 34.50, 1, "ildmlfy", new String[]{"JPEGMAFIA"}, new String[]{"Rap", "Experimental"});
        insertProduit(db, "Hyperyouth", 29.99, 1, "hyperyouth", new String[]{"Joey Valence & Brae"}, new String[]{"Rap", "Electro"});
        insertProduit(db, "I AM MUSIC", 37.99, 1, "iammusic", new String[]{"Playboi Carti"}, new String[]{"Rap", "Trap", "Electro"});
    }

    private void insertProduit(SQLiteDatabase db, String nom, double prix, int idC, String imageName, String[] artistes, String[] genres) {
        ContentValues vP = new ContentValues();
        vP.put("nomP", nom);
        vP.put("prix", prix);
        vP.put("idC", idC);
        vP.put("imageName", imageName);
        long idP = db.insert(TABLE_PRODUIT, null, vP);

        for (String nomA : artistes) {
            db.execSQL("INSERT INTO " + TABLE_PRODUIT_ARTISTE + " (idP, idA) SELECT " + idP + ", idA FROM " + TABLE_ARTISTE + " WHERE nomA = '" + nomA.replace("'", "''") + "';");
        }

        for (String nomG : genres) {
            db.execSQL("INSERT INTO " + TABLE_PRODUIT_GENRE + " (idP, idG) SELECT " + idP + ", idG FROM " + TABLE_GENRE + " WHERE nomG = '" + nomG.replace("'", "''") + "';");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT_ARTISTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE);
        onCreate(db);
    }
}
