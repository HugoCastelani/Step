package com.enoughspam.step.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DatabaseHelper;

/**
 * Created by Hugo Castelani
 * Date: 24/07/17
 * Time: 13:07
 */

public class DAOHandler {

    private static SQLiteDatabase sSqLiteDatabase;

    private DAOHandler() {}

    public static void init(@NonNull final Context context) {
        sSqLiteDatabase = new DatabaseHelper(context).getWritableDatabase();
    }

    public static void finish() {
        sSqLiteDatabase.close();
    }

    protected static SQLiteDatabase getSqLiteDatabase() {
        if (sSqLiteDatabase != null) return sSqLiteDatabase;
        throw new NullPointerException("You should call init method first.");
    }
}
