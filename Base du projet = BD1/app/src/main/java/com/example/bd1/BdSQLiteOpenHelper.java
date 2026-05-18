package com.example.bd1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BdSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private String requete="create table viticulteur ("
			+ "idV INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "nomV TEXT NOT NULL,"
			+ "niveauV INTEGER NOT NULL);";
	
	
	public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(requete);
		
		db.execSQL("insert into viticulteur (nomV,niveauV) values('Garay',50);");
		
		ContentValues value = new ContentValues();
		value.put("nomV", "Esquerra");
		value.put("niveauV",50);
		db.insert("viticulteur", null, value);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
