package com.enoughspam.step.database.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:25
 */

public class CountryDAO {

    public static final String TABLE = "country";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String MASK = "mask";

    private CountryDAO() {}

    public static Country generate(@NonNull final Cursor cursor) {
        return new Country(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(MASK))
        );
    }

    public static Country findByCode(@NonNegative final int code) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, CODE + " = ?", new String[] {String.valueOf(code)},
                null, null, null);

        Country firstCountry = null;

        if (cursor.moveToFirst()) firstCountry = generate(cursor);

        cursor.close();
        return firstCountry;
    }

    public static Country findByName(@NonNull final String name) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, NAME + " = ?", new String[] {name},
                null, null, null);

        Country country = null;

        if (cursor.moveToFirst()) country = generate(cursor);

        cursor.close();
        return country;
    }

    public static String findCodeByName(@NonNull final String name) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, new String[] {CODE}, NAME + " = ?", new String[] {name},
                null, null, null);

        String code = null;

        if (cursor.moveToFirst()) code = cursor.getString(cursor.getColumnIndex(CODE));

        cursor.close();
        return code;
    }

    public static List<String> getColumnList(@NonNull final String column) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, new String[] {column}, null, null, null, null, null);

        final List<String> stringList = new ArrayList<>();

        while (cursor.moveToNext()) {
            stringList.add(cursor.getString(cursor.getColumnIndex(column)));
        }

        cursor.close();
        return stringList;
    }
}
