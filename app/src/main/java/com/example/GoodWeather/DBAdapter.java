package com.example.GoodWeather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    public static final String TABLE_ID = "_id";
    private static final String DATABASE_NAME = "cities";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "city_table";
    public static final String RUCITYNAME = "rucityname";
    public static final String ENCITYNAME = "encityname";

    private static final String SQL_CREATE_ENTRIES = "create table "
            + TABLE_NAME + " ("
            + TABLE_ID + " integer primary key autoincrement, "
            + RUCITYNAME + " text not null, "
            + ENCITYNAME + " text not null ); ";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    private final Context mcontext;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context context) {
        this.mcontext = context;
        DBHelper = new DatabaseHelper(mcontext);
        db = DBHelper.getWritableDatabase();
    }


    public void insert(String ruCityName, String enCityName) {
        ContentValues cv =  new ContentValues();
        Cursor cursor = db.query(TABLE_NAME, null, RUCITYNAME + " = ?", new String[]{ruCityName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) {
            cv.put(RUCITYNAME, ruCityName);
            cv.put(ENCITYNAME, enCityName);
            db.insert(TABLE_NAME, null, cv);
        }
    }

    public Cursor getAllData() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public String[] getAllCities() {
        Cursor cursor = getAllData();
        int size = -1;
        if (cursor.getCount() <= 0) return null;
        String[] result = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex(RUCITYNAME));
            size++;
            result[size] = city;
        }
        return result;
    }


    public void deleteCity(String ruCityName) {
        db.delete(TABLE_NAME, RUCITYNAME + " = ?", new String[]{ruCityName});
    }
    public boolean check(String ruCityName) {
        Cursor cursor = db.query(TABLE_NAME, null, RUCITYNAME + " = ?", new String[]{ruCityName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) return false;
        else return true;
    }
    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
