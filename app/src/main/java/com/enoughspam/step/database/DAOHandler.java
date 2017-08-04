package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 24/07/17
 * Time: 13:07
 */

public class DAOHandler {

    private static SQLiteDatabase sWideDatabase;
    private static SQLiteDatabase sLocalDatabase;

    private DAOHandler() {}

    public static void init(@NonNull final Context context) {
        sWideDatabase = new WideDatabaseHelper(context).getWritableDatabase();
        sLocalDatabase = new LocalDatabaseHelper(context).getWritableDatabase();
    }

    public static void finish() {
        sWideDatabase.close();
        sLocalDatabase.close();
    }

    public static SQLiteDatabase getWideDatabase() {
        if (sWideDatabase != null) return sWideDatabase;
        throw new NullPointerException("You should call init method first.");
    }

    public static SQLiteDatabase getLocalDatabase() {
        if (sLocalDatabase != null) return sLocalDatabase;
        throw new NullPointerException("You should call init method first.");
    }
}
