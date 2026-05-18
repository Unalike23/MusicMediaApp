package com.example.bd1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        methode1();

    }

    private void methode1(){
        BdSQLiteOpenHelper accesBD = new BdSQLiteOpenHelper(this, "BDViticulteur", null, 1);
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        Cursor curseur;
        curseur = bd.rawQuery("select * from viticulteur;",null);
        curseur.moveToFirst();
        String nomV;
        int niveauV;
        while (!curseur.isAfterLast()){
            nomV = curseur.getString(1);
            niveauV = curseur.getInt(2);
            Log.d("methode1",nomV + " " + niveauV);
            curseur.moveToNext();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}