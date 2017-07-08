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
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getInt(cursor.getColumnIndex("code")),
                cursor.getString(cursor.getColumnIndex("name"))
        );
    }

    public Country findById(int id) {
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

    public List<String> findNameByCode(int code) {
        String sql = "select name from country where code = '" + code + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        List<String> countries = new ArrayList<>();

        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndex("name"));
            countries.add(string);
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

    public List<String> getCountryNameList() {
        String sql = "select name from country";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        List<String> countries = new ArrayList<>();

        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndex("name"));
            countries.add(string);
        }

        cursor.close();
        return countries;
    }

    public String findCodeByName(String name) {
        String sql = "select code from country where name = '" + name + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        String code = null;

        if (cursor.moveToFirst()) {
            code = cursor.getString(cursor.getColumnIndex("code"));
        }

        cursor.close();
        return code;
    }
}
