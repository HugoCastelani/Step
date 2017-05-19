package com.enoughspam.step.database.dao.notRelated;

// created by Hugo on 19/05/17 at 14:07

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.dao.DatabaseHelper;
import com.enoughspam.step.database.domains.ThemeData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

    @NotNull
    private ThemeData generateThemeData(Cursor cursor) {
        return new ThemeData(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ConfigTheme.IS_DARK)) == 1,
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.ConfigTheme.LIGHT_ACCENT_COLOR)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.ConfigTheme.DARK_ACCENT_COLOR))
        );
    }

    public ThemeData getThemeData() {
        String selectQuery = "SELECT " + DatabaseHelper.ConfigTheme.IS_DARK + ", "
                + DatabaseHelper.ConfigTheme.LIGHT_ACCENT_COLOR + ", "
                + DatabaseHelper.ConfigTheme.DARK_ACCENT_COLOR +
                " FROM " + DatabaseHelper.ConfigTheme.TABLE;

        Cursor cursor = getSqLiteDatabase().rawQuery(selectQuery, null);
        ThemeData themeData = generateThemeData(cursor);
        cursor.close();

        return themeData;
    }

    public void setThemeData(ThemeData themeData) {
        String isDarkQuery = "UPDATE " + DatabaseHelper.ConfigTheme.TABLE +
                " SET " + DatabaseHelper.ConfigTheme.IS_DARK +
                " = " + (themeData.isDark() ? 1 : 0);

        String lightAccentQuery = "UPDATE " + DatabaseHelper.ConfigTheme.TABLE +
                " SET " + DatabaseHelper.ConfigTheme.LIGHT_ACCENT_COLOR +
                " = " + themeData.getLightAccentColor();

        String darkAccentQuery = "UPDATE " + DatabaseHelper.ConfigTheme.TABLE +
                " SET " + DatabaseHelper.ConfigTheme.DARK_ACCENT_COLOR +
                " = " + themeData.getDarkAccentColor();

        getSqLiteDatabase().execSQL(isDarkQuery);
        getSqLiteDatabase().execSQL(lightAccentQuery);
        getSqLiteDatabase().execSQL(darkAccentQuery);
    }

}
