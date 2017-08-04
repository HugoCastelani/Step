package com.enoughspam.step.database.wideDao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.State;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:59
 */

public class StateDAO {

    public static final String TABLE = "state";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COUNTRY_ID = "country_id";

    private StateDAO() {}

    public static State generate(@NonNull final Cursor cursor) {
        return new State(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                CountryDAO.findById(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)))
        );
    }

    public static State findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        State state = null;

        if (cursor.moveToFirst()) state = generate(cursor);

        cursor.close();
        return state;
    }

}
