package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.Country;
import com.enoughspam.step.database.domains.abstracts.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:25
 */

public class CountryDAO extends DAO {

    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String MASK = "mask";

    public CountryDAO(@NonNull Context context) {
        super(context, "country", "id");
    }

    /*
        CASTCLASSEXCEPTION DE DOMAIN PRA COUNTRY
     */

    @Override
    public Country generate(@NonNull Cursor cursor) {
        return new Country(
                super.generate(cursor).getId(),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(MASK))
        );
    }

    @Override
    public boolean create(@NonNull Domain domain) {
        Country country = (Country) domain;
        ContentValues contentValues = new ContentValues();

        contentValues.put(CODE, country.getCode());
        contentValues.put(NAME, country.getName());
        contentValues.put(MASK, country.getMask());

        return getSqLiteDatabase().insert(TABLE, null, contentValues) > 0;
    }

    @Override
    public Country findById(@NonNegative int id) {
        return (Country) super.findById(id);
    }

    public Country findByCode(@NonNegative int code) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, CODE + " = ?", new String[] {String.valueOf(code)},
                null, null, null);

        Country firstCountry = null;

        if (cursor.moveToFirst()) firstCountry = generate(cursor);

        cursor.close();
        return firstCountry;
    }

    public Country findByName(@NonNull String name) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, NAME + " = ?", new String[] {name},
                null, null, null);

        Country country = null;

        if (cursor.moveToFirst()) country = generate(cursor);

        cursor.close();
        return country;
    }

    public String findCodeByName(@NonNull String name) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {CODE}, NAME + " = ?", new String[] {name},
                null, null, null);

        String code = null;

        if (cursor.moveToFirst()) code = cursor.getString(cursor.getColumnIndex("code"));

        cursor.close();
        return code;
    }

    public List<String> getCountryNameList() {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {NAME}, null, null, null, null, null);

        List<String> countries = new ArrayList<>();

        while (cursor.moveToNext())
            countries.add(cursor.getString(cursor.getColumnIndex("name")));

        cursor.close();
        return countries;
    }
}
