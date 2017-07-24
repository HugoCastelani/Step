package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.Phone;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public class PhoneDAO {

    public static final String TABLE = "phone";
    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String COUNTRY_ID = "country_id";
    public static final String AREA_CODE = "area_code";
    public static final String USER_ID = "user_id";

    public PhoneDAO() {}

    public static Phone generate(@NonNull final Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREA_CODE)) == 0) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(AREA_CODE)),
                    PersonalDAO.get()
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    PersonalDAO.get()
            );
        }
    }

    public static boolean create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        if (phone.getAreaCode() == 0) {
            values.put(NUMBER, phone.getNumber());
            values.put(COUNTRY_ID, phone.getCountryId());
            values.put(USER_ID, phone.getUser().getId());

        } else {

            values.put(NUMBER, phone.getNumber());
            values.put(AREA_CODE, phone.getAreaCode());
            values.put(USER_ID, phone.getUser().getId());
        }

        return DAOHandler.getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    public static Phone findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) phone = generate(cursor);

        cursor.close();
        return phone;
    }
}
