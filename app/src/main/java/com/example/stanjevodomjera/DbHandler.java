package com.example.stanjevodomjera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "stanje_vodomjera.db";
    private static final String TABLE_StanjeVodomjera = "stanje_vodomjera";
    private static final String KEY_DATUM = "datum";
    private static final String KEY_STANJE = "stanje";
    public DbHandler(Context context)
    {
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_StanjeVodomjera + "("
                + KEY_DATUM + " TEXT,"
                + KEY_STANJE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_StanjeVodomjera);
        // Create tables again
        onCreate(db);
    }

    // Adding new User Details
    public void insertEntry(String datum, String stanje)
    {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_DATUM, datum);
        cValues.put(KEY_STANJE, stanje);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_StanjeVodomjera,null, cValues);
        db.close();
    }

    // Get User Details
    public ArrayList<String> GetEntries()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> entryList = new ArrayList<>();
        String query = "SELECT datum, stanje FROM "+ TABLE_StanjeVodomjera;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext())
        {
            String entry = new String();
            entry = cursor.getString(cursor.getColumnIndex(KEY_DATUM));
            entry += "              ";
            entry += cursor.getString(cursor.getColumnIndex(KEY_STANJE));
            entryList.add(entry);
        }

        /* re-format the list */
        return  entryList;
    }

    public void DeleteEntry(String datum, String stanje)
    {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_StanjeVodomjera,"datum=? and stanje=?",new String[]{datum,stanje});
        db.close();
    }
}