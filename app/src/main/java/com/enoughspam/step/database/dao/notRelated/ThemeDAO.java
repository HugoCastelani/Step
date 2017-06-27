package com.enoughspam.step.database.dao.notRelated;

// created by Hugo on 19/05/17 at 14:07

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.dao.DatabaseHelper;
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
                cursor.getInt(cursor.getColumnIndex("isDark")) == 1,
                cursor.getString(cursor.getColumnIndex("accentColor"))
        );
    }

    public ThemeData getThemeData() {
        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT isDark, accentColor FROM config_theme", null);

        ThemeData themeData = null;
        while (cursor.moveToNext())
            themeData = generateThemeData(cursor);

        cursor.close();
        return themeData;
    }

    public void setThemeData(ThemeData themeData) {
        String isDarkQuery = "UPDATE config_theme " +
                " SET isDark = " + (themeData.isDark() ? 1 : 0);

        String accentQuery = "UPDATE config_theme " +
                "SET accentColor = '" + themeData.getAccentColor() + "'";

        getSqLiteDatabase().execSQL(isDarkQuery);
        getSqLiteDatabase().execSQL(accentQuery);
    }
}
