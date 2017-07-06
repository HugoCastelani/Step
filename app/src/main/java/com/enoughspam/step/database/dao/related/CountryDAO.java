package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:25
 */

public class CountryDAO {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public CountryDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    Country generate(Cursor cursor) {
        return new Country(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("code")),
                cursor.getString(cursor.getColumnIndex("name"))
        );
    }

    public Country findById(long id) {
        String sql = "select * from country where id = " + id;
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        Country country = null;
        while (cursor.moveToNext())
            country = generate(cursor);

        cursor.close();
        return country;
    }

    public List<Country> findByCode(int code) {
        String sql = "select * from country where code = '" + code + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        List<Country> countries = new ArrayList<>();

        while (cursor.moveToNext()) {
            Country country = generate(cursor);
            countries.add(country);
        }

        cursor.close();
        return countries;
    }

    public List<Country> getCountryList() {
        String sql = "select * from country";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        List<Country> countries = new ArrayList<>();

        while (cursor.moveToNext()) {
            Country country = generate(cursor);
            countries.add(country);
        }

        cursor.close();
        return countries;
    }
}
