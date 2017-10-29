package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Area;
import com.enoughspam.step.database.localDao.abstracts.GenericHybridDAO;

/**
 * Created by Hugo Castelani
 * Date: 19/10/17
 * Time: 15:10
 */

public class AreaDAO extends GenericHybridDAO<Area> {
    private static AreaDAO instance;

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String STATE_ID = "state_id";

    @Override
    protected void prepareFields() {
        table = "area";
        node = "areas";
        aClass = Area.class;
    }

    private AreaDAO() {}

    public static AreaDAO get() {
        if (instance == null) instance = new AreaDAO();
        return instance;
    }

    @Override
    protected Area generate(@NonNull final Cursor cursor) {
        return new Area(
                cursor.getInt(cursor.getColumnIndex(id)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                StateDAO.get().findByColumn(StateDAO.id,
                        cursor.getString(cursor.getColumnIndex(STATE_ID)))
        );
    }

    @Override
    public AreaDAO create(@NonNull final Area area) {
        ContentValues values = new ContentValues();

        values.put(id, area.getID());
        values.put(CODE, area.getCode());
        values.put(NAME, area.getName());
        values.put(STATE_ID, area.getStateID());

        DAOHandler.getLocalDatabase().insert(table, null, values);
        return instance;
    }

    public AreaDAO update(@NonNull final Area area) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public int exists(@NonNull Area area) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
