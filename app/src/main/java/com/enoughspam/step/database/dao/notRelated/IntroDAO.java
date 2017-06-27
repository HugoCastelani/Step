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
        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT showIntro FROM config_intro", null);

        boolean showIntro = false;
        while (cursor.moveToNext()) {
            Log.e("StepLog", "SHOW_INTRO? " + cursor.getColumnIndex("showIntro"));
            showIntro = (cursor.getColumnIndex("showIntro")) == 1;
        }

        cursor.close();
        return /*showIntro*/ true;
    }

    public void setShowIntro(boolean showIntro) {
        String showIntroQuery = "UPDATE config_intro " +
                " SET showIntro = " + (showIntro ? 1 : 0) + "";

        getSqLiteDatabase().execSQL(showIntroQuery);
    }

}
