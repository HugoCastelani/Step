package com.enoughspam.step.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericHybridDAO;
import com.enoughspam.step.database.domain.Area;

/**
 * Created by Hugo Castelani
 * Date: 19/10/17
 * Time: 15:10
 */

public class LAreaDAO extends GenericHybridDAO<Area> {
    private static LAreaDAO instance;

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String STATE_KEY = "state_key";

    @Override
    protected void prepareFields() {
        table = "area";
        node = "areas";
        aClass = Area.class;
    }

    private LAreaDAO() {}

    public static LAreaDAO get() {
        if (instance == null) instance = new LAreaDAO();
        return instance;
    }

    @Override
    protected Area generate(@NonNull final Cursor cursor) {
        return new Area(
                cursor.getString(cursor.getColumnIndex(key)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(STATE_KEY))
        );
    }

    @Override
    public LAreaDAO create(@NonNull final Area area) {
        final ContentValues values = new ContentValues();

        values.put(key, area.getKey());
        values.put(CODE, area.getCode());
        values.put(NAME, area.getName());
        values.put(STATE_KEY, area.getStateKey());

        DAOHandler.getLocalDatabase().insert(table, null, values);

        return this;
    }

    @Override @Deprecated
    public LAreaDAO update(@NonNull final Area area) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public String exists(@NonNull Area area) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
