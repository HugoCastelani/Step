package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Country;
import com.enoughspam.step.database.localDao.abstracts.GenericHybridDAO;

/**
 * Created by Hugo Castelani
 * Date: 19/10/17
 * Time: 16:59
 */

public class CountryDAO extends GenericHybridDAO<Country> {
    private static CountryDAO instance;

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

    private CountryDAO() {}

    public static CountryDAO get() {
        if (instance == null) instance = new CountryDAO();
        return instance;
    }

    @Override
    protected Country generate(@NonNull final Cursor cursor) {
        return new Country(
                cursor.getInt(cursor.getColumnIndex(id)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(ISO)),
                cursor.getString(cursor.getColumnIndex(MASK))
        );
    }

    @Override
    public CountryDAO create(@NonNull final Country country) {
        ContentValues values = new ContentValues();

        values.put(id, country.getID());
        values.put(CODE, country.getCode());
        values.put(NAME, country.getName());
        values.put(ISO, country.getISO());
        values.put(MASK, country.getMask());

        DAOHandler.getLocalDatabase().insert(table, null, values);
        return instance;
    }

    public CountryDAO update(@NonNull final Country country) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public int exists(@NonNull Country country) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
