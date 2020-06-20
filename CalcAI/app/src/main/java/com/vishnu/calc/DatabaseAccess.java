package com.vishnu.calc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    boolean fine = false;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes(String query) {
        if (!(database.isOpen())) {
            open();
        }
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        System.out.println("Executing query : " + query);
        try {
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(0));
                cursor.moveToNext();
            }
            if (list.isEmpty()) {
                list.add("No datas in following information");
            }

        } catch (android.database.sqlite.SQLiteException e) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;

    }

    public void addFormula(String LHS, String RHS) {
        if (!(database.isOpen())) {
            open();
        }
        try {
            ContentValues values = new ContentValues();
            values.put("LHS", LHS);
            values.put("RHS", RHS);
            database.insert("userformula", null, values);
        } catch (NullPointerException e) {
            openHelper.close();
        }

    }

}

