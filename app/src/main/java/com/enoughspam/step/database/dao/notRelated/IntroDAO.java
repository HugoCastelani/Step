package com.enoughspam.step.database.dao.notRelated;

// created by Hugo on 27/05/17 at 11:25

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.enoughspam.step.database.dao.DatabaseHelper;

public class IntroDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public IntroDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    public boolean showIntro() {
        String selectQuery = "SELECT " + DatabaseHelper.ConfigIntro.SHOW_INTRO +
                " FROM " + DatabaseHelper.ConfigIntro.TABLE;

        Cursor cursor = getSqLiteDatabase().rawQuery(selectQuery, null);

        boolean showIntro = false;
        while (cursor.moveToNext()) {
            Log.e("StepLog", "SHOW_INTRO? " + cursor.getColumnIndex(DatabaseHelper.ConfigIntro.SHOW_INTRO));
            showIntro = (cursor.getColumnIndex(DatabaseHelper.ConfigIntro.SHOW_INTRO)) == 1;
        }

        cursor.close();
        return /*showIntro*/ true;
    }

    public void setShowIntro(boolean showIntro) {
        String showIntroQuery = "UPDATE " + DatabaseHelper.ConfigIntro.TABLE +
                " SET " + DatabaseHelper.ConfigIntro.SHOW_INTRO +
                " = " + (showIntro ? 1 : 0) + "";

        getSqLiteDatabase().execSQL(showIntroQuery);
    }

}
