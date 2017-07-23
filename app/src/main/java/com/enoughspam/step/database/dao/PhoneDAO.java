package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.Phone;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public class PhoneDAO extends DAO<Phone> {

    public static final String NUMBER = "number";
    public static final String COUNTRYID = "country_id";
    public static final String AREACODE = "area_code";
    public static final String USERID = "user_id";

    private PersonalDAO personalDAO;

    public PhoneDAO(@NonNull final Context context) {
        super(context, "phone");
        personalDAO = new PersonalDAO(context);
    }

    @Override
    public Phone generate(@NonNull final Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREACODE)) == 0) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(AREACODE)),
                    personalDAO.get()
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getInt(cursor.getColumnIndex(COUNTRYID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    personalDAO.get()
            );
        }
    }

    @Override
    public boolean create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        if (phone.getAreaCode() == 0) {
            values.put(NUMBER, phone.getNumber());
            values.put(COUNTRYID, phone.getCountryId());
            values.put(USERID, phone.getUser().getId());

        } else {

            values.put(NUMBER, phone.getNumber());
            values.put(AREACODE, phone.getAreaCode());
            values.put(USERID, phone.getUser().getId());
        }

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    @Override
    public boolean update(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        values.put(NUMBER, phone.getNumber());
        values.put(COUNTRYID, phone.getCountryId());
        values.put(AREACODE, phone.getAreaCode());
        values.put(USERID, phone.getUser().getId());

        return getSqLiteDatabase().update(TABLE, values,
                ID + " = ?", new String[] {String.valueOf(phone.getId())}) > 0;
    }
}
