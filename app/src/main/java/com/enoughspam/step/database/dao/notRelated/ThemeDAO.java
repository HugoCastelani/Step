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
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ConfigTheme.IS_DARK)) == 1,
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.ConfigTheme.ACCENT_COLOR))
        );
    }

    public ThemeData getThemeData() {
        String selectQuery = "SELECT " + DatabaseHelper.ConfigTheme.IS_DARK + ", "
                + DatabaseHelper.ConfigTheme.ACCENT_COLOR +
                " FROM " + DatabaseHelper.ConfigTheme.TABLE;

        Cursor cursor = getSqLiteDatabase().rawQuery(selectQuery, null);

        ThemeData themeData = null;
        while (cursor.moveToNext())
            themeData = generateThemeData(cursor);

        cursor.close();
        return themeData;
    }

    public void setThemeData(ThemeData themeData) {
        String isDarkQuery = "UPDATE " + DatabaseHelper.ConfigTheme.TABLE +
                " SET " + DatabaseHelper.ConfigTheme.IS_DARK +
                " = '" + (themeData.isDark() ? 1 : 0) + "'";

        String accentQuery = "UPDATE " + DatabaseHelper.ConfigTheme.TABLE +
                " SET " + DatabaseHelper.ConfigTheme.ACCENT_COLOR +
                " = '" + themeData.getAccentColor() + "'";

        getSqLiteDatabase().execSQL(isDarkQuery);
        getSqLiteDatabase().execSQL(accentQuery);
    }

}
