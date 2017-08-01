package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Phone;

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

    private PhoneDAO() {}

    public static Phone generate(@NonNull final Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREA_CODE)) == 0) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    CountryDAO.findById(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)))
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    AreaDAO.findByCode(cursor.getInt(cursor.getColumnIndex(AREA_CODE)))
            );
        }
    }

    public static long create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        if (phone.getArea().getCode() == 0) {
            values.put(NUMBER, phone.getNumber());
            values.put(COUNTRY_ID, phone.getCountry().getId());

        } else {

            values.put(NUMBER, phone.getNumber());
            values.put(AREA_CODE, phone.getArea().getCode());
        }

        return DAOHandler.getSqLiteDatabase().insert(TABLE, null, values);
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

    public static int exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getCountry() == null) {
            final String areaCode = String.valueOf(phone.getArea().getCode());

            cursor = DAOHandler.getSqLiteDatabase().query(TABLE, null,
                    NUMBER + " = ? AND " + AREA_CODE + " = ?", new String[] {number, areaCode},
                    null, null, null);

        } else {

            final String countryId = String.valueOf(phone.getCountry().getId());

            cursor = DAOHandler.getSqLiteDatabase().query(TABLE, null,
                    NUMBER + " = ? AND " + COUNTRY_ID + " = ? ", new String[] {number, countryId},
                    null, null, null);
        }

        Phone matchingPhone = null;

        if (cursor.moveToFirst()) matchingPhone = generate(cursor);

        cursor.close();

        if (matchingPhone == null) return -1;
        else return matchingPhone.getId();
    }
}
