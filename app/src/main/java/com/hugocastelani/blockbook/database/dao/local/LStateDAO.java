package com.hugocastelani.blockbook.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericHybridDAO;
import com.hugocastelani.blockbook.database.domain.State;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:59
 */

public final class LStateDAO extends GenericHybridDAO<State> {
    private static LStateDAO instance;

    private static final String NAME = "name";
    private static final String COUNTRY_KEY = "country_key";

    @Override
    protected void prepareFields() {
        table = "state";
        node = "states";
        aClass = State.class;
    }

    private LStateDAO() {}

    public static LStateDAO get() {
        if (instance == null) instance = new LStateDAO();
        return instance;
    }

    @Override
    public State generate(@NonNull final Cursor cursor) {
        return new State(
                cursor.getString(cursor.getColumnIndex(key)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(COUNTRY_KEY))
        );
    }

    @Override
    public LStateDAO create(@NonNull final State state) {
        final ContentValues values = new ContentValues();

        values.put(key, state.getKey());
        values.put(NAME, state.getName());
        values.put(COUNTRY_KEY, state.getCountryKey());

        DAOHandler.getLocalDatabase().insert(table, null, values);

        return this;
    }

    @Override @Deprecated
    public LStateDAO update(@NonNull State state) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public String exists(@NonNull State state) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
