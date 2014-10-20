package com.mobile.average;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBase  extends SQLiteOpenHelper{

	static final String DATABASE_NAME = "RendimientoAcademico";
	
	static final String TABLA_REGISTRO = "Registro";
	static final String col_reg_id = "REGISTRO_ID";
	static final String col_reg_name = "NOMBRE";
	
	static final String TABLA_MATERIAS = "Materias";
	static final String col_mat_reg_id = "REGISTRO_ID";
	static final String col_mat_name = "NOMBRE";
	static final String col_mat_id = "MATERIA_ID";
	static final String col_mat_cred = "NCREDITOS";
	
	static final String TABLA_NOTAS = "Notas";
	static final String col_not_id = "NOTA_ID";
	static final String col_not_mat_id = "MATERIA_ID";
	static final String col_not_name = "NOMBRE"; //example: Parcial 1
	static final String col_not_percentage = "PORCENTAJE";
	static final String col_not_nota = "NOTA";
	
	
	public SQLiteDataBase(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ TABLA_REGISTRO + " (" + col_reg_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + col_reg_name + " TEXT );");
		db.execSQL("CREATE TABLE "+ TABLA_MATERIAS + " (" + col_mat_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + col_mat_name + " TEXT , " + col_mat_reg_id + " INTEGER , "+col_mat_cred+" INTEGER );");
		db.execSQL("CREATE TABLE "+ TABLA_NOTAS + " (" + col_not_id+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ col_not_name + " TEXT , " + col_not_mat_id + " INTEGER , " + col_not_percentage + " REAL , " + col_not_nota + " REAL );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> getAllNames(){
        List<String> cadenas = new ArrayList<String>();
         
        // Select All Query
        String selectQuery = "SELECT  "+col_reg_name+" FROM " + TABLA_REGISTRO;
      
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                cadenas.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
        return cadenas;
    }

	public List<Notas> getNotesOf(Integer integer) {
		// TODO Auto-generated method stub
		List<Notas> cadenas = new ArrayList<Notas>();
        
        // Select All Query
        String selectQuery = "SELECT  TN."+col_not_name+" , TN."+col_not_nota+" FROM " + TABLA_NOTAS + 
        		" AS TN WHERE TN."+col_not_mat_id+" = "+integer;
      
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Notas nota = new Notas(cursor.getString(0), cursor.getFloat(1));
                cadenas.add(nota);
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
        return cadenas;
	}
	
	public List<Notas> getFullNotesOf(Integer integer) {
		// TODO Auto-generated method stub
		List<Notas> cadenas = new ArrayList<Notas>();
        
        // Select All Query
        String selectQuery = "SELECT  TN."+col_not_name+" , TN."+col_not_percentage+" , TN."+col_not_nota+" , TN."+col_not_id+" FROM " + TABLA_NOTAS + 
        		" AS TN WHERE TN."+col_not_mat_id+" = "+integer;
      
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Notas nota = new Notas(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2));
                nota.ID = cursor.getInt(3);
                cadenas.add(nota);
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
        return cadenas;
	}
	
	public void AddMateria(String dataname, String name, int creditos) {
		// TODO Auto-generated method stub
		String sqlquery = "SELECT TR."+col_reg_id+" FROM "+TABLA_REGISTRO+" AS TR WHERE TR."+col_reg_name+" = '"+dataname+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlquery, null);
        int dataname_id=-1;
		if (cursor.moveToFirst()) {
            do {
                dataname_id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
		cursor.close();db.close();
		db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(col_mat_name, name);
		values.put(col_mat_reg_id, dataname_id);
		values.put(col_mat_cred, creditos);
		db.insert(TABLA_MATERIAS, null, values);
		db.close();
	}

	public void AddNotas(String dataname, String name, String[] nota_name,
			float[] nota) {
		// TODO Auto-generated method stub
		String sqlquery = "SELECT TM."+col_mat_id+" FROM "+TABLA_MATERIAS+" AS TM, "+TABLA_REGISTRO+" AS TR WHERE TR."+
		col_reg_name+" = '"+dataname+"' AND TR."+col_reg_id+" = TM."+col_mat_reg_id+" AND TM."+col_mat_name+" = '"+name+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlquery, null);
        int materia_id=0;
		if (cursor.moveToFirst()) {
            do {
                materia_id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
		cursor.close();db.close();
		db = getWritableDatabase();
		for (int i = 0; i < nota.length; i++) {
			ContentValues values = new ContentValues();
			values.put(col_not_name, nota_name[i]);
			values.put(col_not_mat_id, materia_id);
			values.put(col_not_percentage, nota[i]);
			values.put(col_not_nota, 0.0f);
			db.insert(TABLA_NOTAS, null, values);
		}
	}

	public void deleteMateria(Materia materia) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLA_MATERIAS, col_mat_id+"="+materia.getId(), null);
		
		db.close();
	}

	public void deleteNotas(List<Notas> list) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			db.delete(TABLA_NOTAS, col_not_id+"="+list.get(i).ID, null);
		}
		db.close();
	}
	
}
