package com.enoughspam.step.database.dao.notRelated;

// created by Hugo on 19/05/17 at 14:07

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.ThemeData;

public class ThemeDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public ThemeDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    private ThemeData generateThemeData(Cursor cursor) {
        return new ThemeData(
                cursor.getInt(cursor.getColumnIndex("is_dark")) == 1,
                cursor.getString(cursor.getColumnIndex("accent_color"))
        );
    }

    public ThemeData getThemeData() {
        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT is_dark, accent_color FROM config_theme", null);

        ThemeData themeData = null;
        while (cursor.moveToNext())
            themeData = generateThemeData(cursor);

        cursor.close();
        return themeData;
    }

    public void setThemeData(ThemeData themeData) {
        String isDarkQuery = "UPDATE config_theme " +
                " SET is_dark = " + (themeData.isDark() ? 1 : 0);

        String accentQuery = "UPDATE config_theme " +
                "SET accent_color = '" + themeData.getAccentColor() + "'";

        getSqLiteDatabase().execSQL(isDarkQuery);
        getSqLiteDatabase().execSQL(accentQuery);
    }
}
