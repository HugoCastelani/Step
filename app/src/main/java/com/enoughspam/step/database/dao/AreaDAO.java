package com.enoughspam.step.database.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Area;
import com.enoughspam.step.database.domain.Country;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:06
 */

public class AreaDAO {

    public static final String TABLE = "area";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String STATE_ID = "state_id";

    private AreaDAO() {}

    public static Area generate(@NonNull final Cursor cursor) {
        return new Area(
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                StateDAO.findById(cursor.getInt(cursor.getColumnIndex(STATE_ID)))
        );
    }

    public static Area findByCode(@NonNegative final int code) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, CODE + " = ?", new String[] {String.valueOf(code)},
                null, null, null);

        Area area = null;

        if (cursor.moveToFirst()) area = generate(cursor);

        cursor.close();
        return area;
    }

    public static boolean mathingArea(@NonNull final Country country, @NonNull final String code) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, new String[] {CODE}, CODE + " = ?", new String[] {code},
                null, null, null);

        if (cursor.moveToFirst()) {
            if (generate(cursor).getState().getCountry().getId() == country.getId()) {
                return true;
            }
        }

        return false;
    }
}
