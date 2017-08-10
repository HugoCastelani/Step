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
    private static Context sContext;

    private static SQLiteDatabase sWideDatabase;
    private static SQLiteDatabase sLocalDatabase;

    private DAOHandler() {}

    public static void init(@NonNull final Context context) {
        sContext = context;
        sWideDatabase = new WideDatabaseHelper(sContext).getWritableDatabase();
        sLocalDatabase = new LocalDatabaseHelper(sContext).getWritableDatabase();
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

    public static Context getContext() {
        if (sLocalDatabase != null) return sContext;
        throw new NullPointerException("You should call init method first.");
    }
}
