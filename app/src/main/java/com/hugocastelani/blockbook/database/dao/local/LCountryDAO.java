package com.hugocastelani.blockbook.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericHybridDAO;
import com.hugocastelani.blockbook.database.domain.Country;

/**
 * Created by Hugo Castelani
 * Date: 19/10/17
 * Time: 16:59
 */

public final class LCountryDAO extends GenericHybridDAO<Country> {
    private static LCountryDAO instance;

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String ISO = "iso";
    public static final String MASK = "mask";

    @Override
    protected void prepareFields() {
        table = "country";
        node = "countries";
        aClass = Country.class;
    }

    private LCountryDAO() {}

    public static LCountryDAO get() {
        if (instance == null) instance = new LCountryDAO();
        return instance;
    }

    @Override
    protected Country generate(@NonNull final Cursor cursor) {
        return new Country(
                cursor.getString(cursor.getColumnIndex(key)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(ISO)),
                cursor.getString(cursor.getColumnIndex(MASK))
        );
    }

    @Override
    public LCountryDAO create(@NonNull final Country country) {
        final ContentValues values = new ContentValues();

        values.put(key, country.getKey());
        values.put(CODE, country.getCode());
        values.put(NAME, country.getName());
        values.put(ISO, country.getISO());
        values.put(MASK, country.getMask());

        DAOHandler.getLocalDatabase().insert(table, null, values);

        return this;
    }

    @Override @Deprecated
    public LCountryDAO update(@NonNull final Country country) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public String exists(@NonNull Country country) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
