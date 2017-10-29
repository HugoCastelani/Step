package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.State;
import com.enoughspam.step.database.localDao.abstracts.GenericHybridDAO;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:59
 */

public class StateDAO extends GenericHybridDAO<State> {
    private static StateDAO instance;

    private static final String NAME = "name";
    private static final String COUNTRY_ID = "country_id";

    @Override
    protected void prepareFields() {
        table = "state";
        node = "states";
        aClass = State.class;
    }

    private StateDAO() {}

    public static StateDAO get() {
        if (instance == null) instance = new StateDAO();
        return instance;
    }

    @Override
    public State generate(@NonNull final Cursor cursor) {
        return new State(
                cursor.getInt(cursor.getColumnIndex(id)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                CountryDAO.get().findByColumn(CountryDAO.id,
                        cursor.getString(cursor.getColumnIndex(COUNTRY_ID)))
        );
    }

    @Override
    public StateDAO create(@NonNull final State state) {
        ContentValues values = new ContentValues();

        values.put(id, state.getID());
        values.put(NAME, state.getName());
        values.put(COUNTRY_ID, state.getCountryID());

        DAOHandler.getLocalDatabase().insert(table, null, values);
        return instance;
    }

    @Override
    public StateDAO update(@NonNull State state) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public int exists(@NonNull State state) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
