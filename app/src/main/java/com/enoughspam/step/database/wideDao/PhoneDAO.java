package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
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

    public static int create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        if (phone.getArea().getCode() == 0) {
            values.put(NUMBER, phone.getNumber());
            values.put(COUNTRY_ID, phone.getCountry().getId());

        } else {

            values.put(NUMBER, phone.getNumber());
            values.put(AREA_CODE, phone.getArea().getCode());
        }

        final int id = (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);

        if (id != -1) {
            values.put(ID, id);
            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }

        return id;
    }

    public static Phone findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) phone = generate(cursor);

        cursor.close();
        return phone;
    }

    public static void delete(@NonNull final int id) {
        DAOHandler.getWideDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static int exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getCountry() == null) {
            final String areaCode = String.valueOf(phone.getArea().getCode());

            cursor = DAOHandler.getWideDatabase().query(TABLE, null,
                    NUMBER + " = ? AND " + AREA_CODE + " = ?", new String[] {number, areaCode},
                    null, null, null);

        } else {

            final String countryId = String.valueOf(phone.getCountry().getId());

            cursor = DAOHandler.getWideDatabase().query(TABLE, null,
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
